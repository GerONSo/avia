package com.serrriy.aviascan.main.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serrriy.aviascan.R
import com.serrriy.aviascan.main.MainViewModel
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.utils.OutlinedTextFieldColors

@Composable
fun rememberLoginViewModel(): LoginViewModel {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(
                    dataStoreRepository = DataStoreRepository(context),
                ) as T
            }
        }
    }
    return viewModel(factory = factory)
}

@Composable
fun LoginScreen(
    mainViewModel: MainViewModel,
    viewModel: LoginViewModel = rememberLoginViewModel(),
    onRegisterButtonClicked: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initDataStore(context)
    }

    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            mainViewModel.refresh(needRefresh = true)
            viewModel.clearLoginSuccess()
        }
    }

    Column(Modifier.padding(16.dp).imePadding()) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.aviascan),
            contentDescription = "logo",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.3f))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.emailChanged(it) },
            label = { Text("Email") },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.passwordChanged(it) },
            label = { Text("Password") },
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }){
                    Icon(imageVector  = image, description)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.ButtonColor)
            ),
            modifier = Modifier
                .fillMaxWidth().height(50.dp),
            onClick = {
                viewModel.loginButtonClicked()
            }
        ) {
            Text("Login")
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.height(50.dp).padding(top = 16.dp).align(Alignment.CenterHorizontally),
            onClick = {
                onRegisterButtonClicked()
            }
        ) {
            Text(
                text = "No account? Register now",
                color = Color.Black,
                fontWeight = FontWeight.Light,
                textDecoration = TextDecoration.Underline,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}