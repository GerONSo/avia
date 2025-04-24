package com.serrriy.aviascan.main.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serrriy.aviascan.R
import com.serrriy.aviascan.main.MainViewModel
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.utils.OutlinedTextFieldColors

@Composable
fun rememberRegistrationViewModel(): RegistrationViewModel {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegistrationViewModel(
                    dataStoreRepository = DataStoreRepository(context),
                ) as T
            }
        }
    }
    return viewModel(factory = factory)
}

@Composable
fun RegistrationScreen(
    mainViewModel: MainViewModel,
    viewModel: RegistrationViewModel = rememberRegistrationViewModel(),
    onBackClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            mainViewModel.processAuth()
        }
    }

    Column(Modifier.padding(16.dp).imePadding()) {

        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Go Back",
            modifier = Modifier
                .padding(top = 32.dp)
                .clickable { onBackClicked() }
        )
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.aviascan),
            contentDescription = "logo",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        OutlinedTextField(
            value = uiState.login,
            onValueChange = { viewModel.loginChanged(it) },
            label = { Text("Login") },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors(),
            supportingText = {
                if (uiState.login.isEmpty() && uiState.registerButtonPressedOnce) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Should not be empty",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (uiState.login.isEmpty() && uiState.registerButtonPressedOnce) {
                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
                }
            },
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.emailChanged(it) },
            label = { Text("Email") },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = {
                if (uiState.email.isEmpty() && uiState.registerButtonPressedOnce) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Should not be empty",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (uiState.email.isEmpty() && uiState.registerButtonPressedOnce) {
                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
                }
            },
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.passwordChanged(it) },
            label = { Text("Password") },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            supportingText = {
                if (uiState.password.isEmpty() && uiState.registerButtonPressedOnce) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Should not be empty",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (uiState.password.isEmpty() && uiState.registerButtonPressedOnce) {
                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
                }
            },
        )

        OutlinedTextField(
            value = uiState.repeatPassword,
            onValueChange = { viewModel.repeatPasswordChanged(it) },
            label = { Text("Repeat password") },
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            supportingText = {
                if (!uiState.passwordMatch && uiState.registerButtonPressedOnce) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Passwords don't match",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (!uiState.passwordMatch && uiState.registerButtonPressedOnce) {
                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
                }
            },
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.ButtonColor)
            ),
            enabled = uiState.registerButtonActive,
            modifier = Modifier
                .fillMaxWidth().height(50.dp),
            onClick = {
                viewModel.registerButtonPressed()
            }
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}