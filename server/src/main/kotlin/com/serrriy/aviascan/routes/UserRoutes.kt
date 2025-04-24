package com.serrriy.aviascan.routes

import com.serrriy.aviascan.data.user.ChangeUserInfoRequest
import com.serrriy.aviascan.data.user.CreateUserRequest
import com.serrriy.aviascan.data.user.GetUserRequest
import com.serrriy.aviascan.data.user.ListUserResponse
import com.serrriy.aviascan.database.ImageRepository
import com.serrriy.aviascan.data.user.SubscribeRequest
import com.serrriy.aviascan.data.user.TokenRefreshRequest
import com.serrriy.aviascan.data.user.TokenResponse
import com.serrriy.aviascan.data.user.UserResponse
import com.serrriy.aviascan.data.user.UserWithTokenResponse
import com.serrriy.aviascan.database.UserRepository
import com.serrriy.aviascan.utils.JwtConfig
import com.serrriy.aviascan.routes.decodeMultipartForm
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt(12)) // 12 is cost factor
}

fun validatePassword(plainPassword: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(plainPassword, hashedPassword)
}

fun Route.userRoutes(imageRepo: ImageRepository) {
    route("/users") {
        post("/create") {
            val createUser = call.receive<CreateUserRequest>()

            val user = UserRepository().create(
                name = createUser.name,
                email = createUser.email,
                encodedPassword = hashPassword(createUser.password),
            )
            
            val accessToken = JwtConfig.generateAccessToken(user.id)
            val refreshToken =  JwtConfig.generateRefreshToken(user.id)
            
            val response = UserWithTokenResponse(
                token = TokenResponse(accessToken = accessToken, refreshToken = refreshToken),
                userId = user.id,
                userName = user.name
            )
            call.respond(HttpStatusCode.Created, response)
        }

        post("/login") {
            val getUser = call.receive<GetUserRequest>()

            val user = UserRepository().getByEmail(getUser.email)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "The user ${getUser.email} does not exist")
                return@post
            }
            if (!validatePassword(getUser.password, user.password)) {
                call.respond(HttpStatusCode.Forbidden, "The user password is wrong")
                return@post
            }
            
            val accessToken = JwtConfig.generateAccessToken(user.id)
            val refreshToken =  JwtConfig.generateRefreshToken(user.id)
            
            val response = UserWithTokenResponse(
                token = TokenResponse(accessToken = accessToken, refreshToken = refreshToken),
                userId = user.id,
                userName = user.name
            )

            call.respond(HttpStatusCode.OK, response)
        }

        authenticate("auth-jwt") {
            get("/list/{id}") {
                val id = call.parameters["id"]
                val size = call.request.queryParameters["limit"]?.let { it.toInt() + 1 } ?: Int.MAX_VALUE - 1
                val query = call.request.queryParameters["query"] ?: ""
                val (startFromSubscribed, startFrom) = call.request.queryParameters["start_from"]?.let {
                    val parts = it.split("/")
                    Pair(parts[0] == "0", parts[1])
                } ?: Pair(true, UUID(0, 0).toString())
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                var (usersSubscribed, usersNotSubscribed) = UserRepository().listWithSubscriptions(id, query, startFrom, size, startFromSubscribed)
                var startNext: String? = null
                if (usersSubscribed != null && usersSubscribed.size == size) {
                    startNext = "0/${usersSubscribed.last().id}"
                    usersSubscribed = usersSubscribed.dropLast(1)
                } else if (
                    usersNotSubscribed != null &&
                    usersSubscribed != null &&
                    usersSubscribed.size + usersNotSubscribed.size == size
                ) {
                    startNext = "1/${usersNotSubscribed.last().id}"
                    usersNotSubscribed = usersNotSubscribed.dropLast(1)
                } else if (usersNotSubscribed != null && usersNotSubscribed.size == size) {
                    startNext = "1/${usersNotSubscribed.last().id}"
                    usersNotSubscribed = usersNotSubscribed.dropLast(1)
                }
                
                val response = ListUserResponse(
                    subscribedUsers = usersSubscribed?.map { UserResponse(it.id, it.name, it.imageUrl) },
                    users = usersNotSubscribed?.map { UserResponse(it.id, it.name, it.imageUrl) }, 
                    startFrom = startNext
                )
                call.respond(HttpStatusCode.OK, response)
            }
        }
        
        authenticate("auth-jwt") {
            post("/subscribe") {
                val subscribe = call.receive<SubscribeRequest>()

                UserRepository().subscribe(subscribe.userId, subscribe.subscribeTo)
                call.respond(HttpStatusCode.OK)
            }
        }

        authenticate("auth-jwt") {
            post("/unsubscribe") {
                val subscribe = call.receive<SubscribeRequest>()

                UserRepository().unsubscribe(subscribe.userId, subscribe.subscribeTo)
                call.respond(HttpStatusCode.OK)
            }
        }

        authenticate("auth-jwt") {
            get("/profile/{id}") {
                val id = call.parameters["id"]

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val userProfile = UserRepository().userById(id)

                userProfile ?: run {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val response = UserResponse(
                    id = userProfile.id,
                    name = userProfile.name,
                    email = userProfile.email,
                    imageUrl = userProfile.imageUrl,
                )
                call.respond(HttpStatusCode.OK, response)
            }
        }

        authenticate("auth-jwt") {
            post("/changeUserInfo") {
                val multipart = call.receiveMultipart()
                val (changeUserInfo, imageUpload) = decodeMultipartForm<ChangeUserInfoRequest>(multipart)
                var imageUrl: String? = null
                if (imageUpload != null) {
                    imageUrl = imageRepo.uploadImage(imageUpload.bytes, imageUpload.filename, basePath = "users")
                }

                UserRepository().changeUserName(changeUserInfo.userId, changeUserInfo.name, imageUrl)

                call.respond(HttpStatusCode.OK)
            }
        }

    }

    route("/token") {
        post("/refresh") {
            val refreshToken = call.receive<TokenRefreshRequest>().refreshToken

            try {
                val decoded = JwtConfig.getVerifier().verify(refreshToken)
    
                if (decoded.getClaim("type").asString() != "refresh") {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid token type")
                    return@post
                }
    
                val userId = decoded.subject
                val newAccessToken = JwtConfig.generateAccessToken(userId)
                call.respond(HttpStatusCode.OK, TokenResponse(newAccessToken))
    
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid or expired refresh token")
            }
        }
    }
}
