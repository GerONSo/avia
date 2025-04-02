package com.serrriy.aviascan.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.flywaydb.core.Flyway

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        // val dbUrl = config.property("database.url").getString()
        // val dbUser = config.property("database.user").getString()
        // val dbPassword = config.property("database.password").getString()
        // val dbDriver = config.property("database.driver").getString()

        val flyway = Flyway.configure()
            .dataSource("jdbc:postgresql://127.0.0.1:5432/aviascan", "aviascan_root", "super_secret_password")
            .load()
        flyway.migrate()

        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/aviascan"
            driverClassName = "org.postgresql.Driver"
            username = "aviascan_root"
            password = "super_secret_password"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        return HikariDataSource(config)
    }
}
