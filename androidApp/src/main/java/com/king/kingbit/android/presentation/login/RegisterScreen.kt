package com.king.kingbit.android.presentation.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.king.kingbit.login.presentation.LoginEvent
import com.king.kingbit.login.presentation.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(navController: NavController, loginViewModel: LoginViewModel = koinViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        loginViewModel.event.collect { event ->
            when (event) {
                is LoginEvent.ShowError -> {
                    showDialog = true
                }

                is LoginEvent.Idle -> {
                    showDialog = false
                }

                is LoginEvent.NavigateHome -> {

                }

                LoginEvent.NavigationRegister -> TODO()
            }
        }
    }
    Text("Register")
}