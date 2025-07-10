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
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.king.kingbit.android.design.KingBitButton
import com.king.kingbit.login.presentation.LoginAction
import com.king.kingbit.login.presentation.LoginViewModel
import com.king.kingbit.util.Route

@Composable
fun HomeMainScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = koinViewModel()) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        Log.d("TAGGG", "HomeScreen: $state")
    }
    val coinList = state.topCoin.toList()

    when {
        state.isLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage.isNotEmpty() -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage, color = Color.Red)
            }
        }

        coinList.isEmpty() -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No coins found.", color = Color.Gray, fontSize = 16.sp)
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F8FA)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(coinList, key = { it.id }) { coin ->
                    val animScale = animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 400),
                        label = "coinAnimScale"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(animScale.value)
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(0xFFE3EDF7), Color(0xFFC8E6C9))
                                ),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            AysncImage(
//                                model = coin.imageUrl,
//                                contentDescription = coin.name,
//                                modifier = Modifier.size(48.dp)
//                            )
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(coin.name, fontSize = 20.sp, color = Color(0xFF374151))
                                Text(
                                    coin.symbol.uppercase(),
                                    fontSize = 14.sp,
                                    color = Color(0xFF607D8B)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${"%.2f".format(coin.currentPrice)}",
                                    color = Color(0xFF1B5E20),
                                    fontSize = 18.sp
                                )
                                Text(
                                    "Rank #${coin.marketCapRank}",
                                    color = Color(0xFF546E7A),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SettingsScreen(
    navController: NavController, loginViewModel: LoginViewModel = koinViewModel()
) {
    Column {
        Text("SettingsScreen")
        KingBitButton(
            text = "Logout",
            onClick = {
                loginViewModel.onAction(LoginAction.LogOut)
                navController.popBackStack()
                navController.navigate(Route.Login)
            },
            enabled = true
        )
    }
}