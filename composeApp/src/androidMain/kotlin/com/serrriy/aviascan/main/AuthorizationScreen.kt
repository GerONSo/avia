package com.serrriy.aviascan.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.serrriy.aviascan.main.auth.LoginScreen
import com.serrriy.aviascan.main.registration.RegistrationScreen

@Composable
fun AuthorizationScreen(viewModel: MainViewModel) {
    var isLogin by remember { mutableStateOf(true) }

    if (isLogin) {
        LoginScreen(viewModel) {
            isLogin = false
        }
    } else {
        RegistrationScreen(viewModel) {
            isLogin = true
        }
    }
}