package com.king.kingbit.android.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.king.kingbit.android.presentation.home.bottom_nav.NavItemState
import com.king.kingbit.android.presentation.home.crypto.CoinDetailScreen
import com.king.kingbit.android.presentation.home.crypto.HotCoinsRootScreen
import com.king.kingbit.android.presentation.home.crypto.TopCryptoRootScreen
import com.king.kingbit.android.presentation.home.crypto.SettingsScreen
import com.king.kingbit.android.presentation.home.game.GameDetailScreen
import com.king.kingbit.android.presentation.home.game.GameHubScreen
import com.king.kingbit.util.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    parentNavController: NavController,
) {
    val tabNavController = rememberNavController()

    val currentBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val shouldHideBottomBar by remember(currentRoute) {
        derivedStateOf {
            val currentRouteName = currentRoute
                ?.substringAfterLast('.')
                ?.substringBefore('?')
            currentRouteName in listOf("CoinDetail", "GameDetail", "TopCrypto")
        }
    }

    Scaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        bottomBar = {
            if(!shouldHideBottomBar) {
                NavigationBarBottom(tabNavController)
            }
        }

    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = Route.HotCoins,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable<Route.HotCoins> { HotCoinsRootScreen(navController = tabNavController) }
            composable<Route.TopCrypto> {
                TopCryptoRootScreen(navController = tabNavController)
            }
            composable<Route.CoinDetail> { backStackEntry ->
                val coinId = backStackEntry.arguments?.getString("coinId")
                CoinDetailScreen(coinId = coinId.toString(), tabNavController = tabNavController)
            }
            composable<Route.Game> { GameHubScreen(navController = tabNavController) }
            composable<Route.GameDetail>{ backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId")
                GameDetailScreen(gameId = gameId, navController = tabNavController)
            }
            composable<Route.Settings> { SettingsScreen(parentNavController) }
        }
    }
}

@Composable
fun NavigationBarBottom(tabNavController : NavController){
    var navBarState by rememberSaveable { mutableIntStateOf(0) }
    val navigationItems = mutableListOf(
        NavItemState("Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavItemState("Game", Icons.Filled.PlayArrow, Icons.Outlined.PlayArrow),
        NavItemState("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    NavigationBar(
        containerColor = NavigationBarDefaults.containerColor,
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = navBarState == index,
                onClick = {
                    navBarState = index
                    when(index){
                        0 -> {
                            tabNavController.navigate(Route.HotCoins)
                        }
                        1 -> {
                            tabNavController.navigate(Route.Game)
                        }
                        2-> {
                            tabNavController.navigate(Route.Settings)
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (navBarState == index) item.selectedIcon else item.unSelectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }

}