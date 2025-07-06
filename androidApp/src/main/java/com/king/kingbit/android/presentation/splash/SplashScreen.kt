package com.king.kingbit.android.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.king.kingbit.android.R
import com.king.kingbit.login.presentation.LoginViewModel
import com.king.kingbit.util.Route
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = isSystemInDarkTheme(),
    loginViewModel: LoginViewModel = koinViewModel()
) {
    val userState by loginViewModel.userState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LaunchedEffect(userState.isAuthenticated) {
            delay(2000)
            if (userState.isAuthenticated) {
                navController.navigate(Route.Home) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            }else {
                navController.popBackStack()
                navController.navigate(Route.Login)
            }
        }

        Image(
            painter = painterResource(if (isDarkMode) R.drawable.logo_king_bit_big_size_dark else R.drawable.logo_king_bit_big_size),
            contentDescription = "KingBit logo"
        )
    }
}