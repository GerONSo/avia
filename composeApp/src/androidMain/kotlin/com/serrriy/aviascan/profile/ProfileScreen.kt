package com.serrriy.aviascan.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serrriy.aviascan.R
import com.serrriy.aviascan.main.MainViewModel
import com.serrriy.aviascan.profile.achievements.AchievementsScreen
import com.serrriy.aviascan.repositories.DataStoreRepository

@Composable
fun rememberProfileViewModel(): ProfileViewModel {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(dataStoreRepository = DataStoreRepository(context)) as T
            }
        }
    }
    return viewModel(factory = factory)
}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = rememberProfileViewModel(),
    mainViewModel: MainViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getUserName()
    }

    Column(Modifier.fillMaxWidth().fillMaxHeight()) {
        Row(Modifier.fillMaxWidth().padding(top = 26.dp)) {
            TextField(
                value = uiState.userName,
                onValueChange = { viewModel.updateName(it) },
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(start = 16.dp, end = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black,
                    selectionColors = TextSelectionColors(
                        handleColor = Color.Black,
                        backgroundColor = Color.Transparent
                    ),
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                )
            )

            Spacer(Modifier.weight(1f))

            if (uiState.userName != uiState.lastUserName) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Confirm new name",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 32.dp)
                        .clickable {
                            viewModel.changeName()
                        }
                )
            }
        }
        uiState.email?.let { email ->
            Text(
                text = "Email: $email",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp, start = 32.dp)
            )
        }


        AchievementsScreen(viewModel)

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.Negative)
            ),
            modifier = Modifier
                .fillMaxWidth().height(76.dp).padding(start = 26.dp, end = 26.dp, top = 26.dp),
            onClick = {
                mainViewModel.logout()
            }
        ) {
            Text("Logout")
        }
    }
}