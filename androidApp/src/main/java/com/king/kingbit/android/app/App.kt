package com.king.kingbit.android.app


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.king.kingbit.util.Route
import com.king.kingbit.android.presentation.login.LoginScreen
import com.king.kingbit.android.presentation.login.RegisterScreen
import com.king.kingbit.android.presentation.splash.SplashScreen


@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Route.Splash
    ) {
        composable<Route.Splash> {
            SplashScreen(navController)
        }

        composable<Route.Login> {
            LoginScreen(navController)
        }
        composable<Route.Register> {
            RegisterScreen(navController)
        }
        composable<Route.Home> {

        }
    }
}
