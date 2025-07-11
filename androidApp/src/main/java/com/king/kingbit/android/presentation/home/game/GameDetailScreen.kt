package com.king.kingbit.android.presentation.home.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun GameDetailScreen(gameId: String?, navController: NavController) {
    val game = gamesList.find { it.id == gameId }
    if (game != null) {
        game.content()
    } else {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Game not found.")
            Button(onClick = { navController.popBackStack() }) { Text("Back") }
        }
    }
}
