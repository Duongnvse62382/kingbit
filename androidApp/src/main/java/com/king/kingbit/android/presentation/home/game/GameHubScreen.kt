package com.king.kingbit.android.presentation.home.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.king.kingbit.android.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.king.kingbit.android.presentation.home.game.rps.RockPaperScissorsGame
import com.king.kingbit.util.Route

// Define the model
class GameItem(
    val id: String,
    val name: String,
    val icon: Int,
    val description: String,
    val content: @Composable () -> Unit
)

val gamesList = listOf(
    GameItem(
        id = "rps",
        name = "Rock Paper Scissors",
        icon = R.drawable.rock_paper_scissors, // or a better game icon if you have
        description = "A fun battle simulation with physics!",
        content = { RockPaperScissorsGame(Modifier.fillMaxSize()) }
    )
    // Add more games here!
)

@Composable
fun GameHubScreen(modifier: Modifier = Modifier, navController: NavController) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "Mini Game Hub",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(bottom = 22.dp)
            )
        }
        items(gamesList, key = { it.id }) { game ->
            Row(
                Modifier
                    .padding(vertical = 10.dp, horizontal = 18.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate(route = Route.GameDetail(game.id)) }
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(game.icon),
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(Modifier.width(18.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        game.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        game.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
