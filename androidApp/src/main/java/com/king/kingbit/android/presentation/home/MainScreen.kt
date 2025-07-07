package com.king.kingbit.android.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.king.kingbit.android.presentation.home.bottom_nav.NavItemState
import com.king.kingbit.android.presentation.home.bottom_nav.screen.HomeMainScreen
import com.king.kingbit.android.presentation.home.bottom_nav.screen.MaiLScreen
import com.king.kingbit.android.presentation.home.bottom_nav.screen.SettingsScreen
import com.king.kingbit.util.Route
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    parentNavController: NavController,
) {
    val tabNavController = rememberNavController()
    var navBarState by rememberSaveable { mutableIntStateOf(0) }


    Scaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Top App Bar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBarBottom(tabNavController)
        }


    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = Route.Home,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable<Route.Home> { HomeMainScreen() }
            composable<Route.Email> { MaiLScreen() }
            composable<Route.Settings> { SettingsScreen() }
        }
    }
}

@Composable
fun NavigationBarBottom(tabNavController : NavController){
    var navBarState by rememberSaveable { mutableIntStateOf(0) }
    val navigationItems = mutableListOf(
        NavItemState("Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavItemState("Email", Icons.Filled.Email, Icons.Outlined.Email),
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
                            tabNavController.navigate(Route.Home)
                        }
                        1 -> {
                            tabNavController.navigate(Route.Email)
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