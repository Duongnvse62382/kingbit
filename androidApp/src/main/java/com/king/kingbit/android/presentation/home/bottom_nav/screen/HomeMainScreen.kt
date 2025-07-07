package com.king.kingbit.android.presentation.home.bottom_nav.screen

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.king.kingbit.home.presentation.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeMainScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = koinViewModel()) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        Log.d("TAGGG", "HomeScreen: $state")
    }
    Text("$state")
}

@Composable
fun MaiLScreen(modifier: Modifier = Modifier) {
    Text("MaiLScreen")
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Text("SettingsScreen")
}