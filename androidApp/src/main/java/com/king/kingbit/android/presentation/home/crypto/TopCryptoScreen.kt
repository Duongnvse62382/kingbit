package com.king.kingbit.android.presentation.home.crypto

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.king.kingbit.android.presentation.home.crypto.components.CoinListItem
import com.king.kingbit.android.presentation.home.crypto.constants.CoinListItemConstants
import com.king.kingbit.home.presentation.crypto.CoinListAction
import com.king.kingbit.home.presentation.crypto.CoinListMoreEvent
import com.king.kingbit.home.presentation.crypto.TopCoinState
import com.king.kingbit.home.presentation.crypto.viewmodel.TopCryptoViewModel
import com.king.kingbit.util.Route.CoinDetail
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopCryptoRootScreen(
    navController: NavController,
    viewModel: TopCryptoViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is CoinListMoreEvent.Error -> {
                    Toast.makeText(
                        context, it.error, Toast.LENGTH_LONG
                    ).show()
                }

                is CoinListMoreEvent.NavigateToCoinDetail -> {
                    navController.navigate(CoinDetail(it.coinId))
                }
            }
        }
    }

    TopCryptoScreen(state = state, onAction = viewModel::onAction)

}

@Composable
fun TopCryptoScreen(
    modifier: Modifier = Modifier,
    state: TopCoinState,
    onAction: (CoinListAction) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(CoinListItemConstants.COIN_WEIGHT)
                            .padding(start = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text("Coin", fontWeight = FontWeight.SemiBold)
                    }

                    Box(
                        modifier = Modifier.weight(CoinListItemConstants.PRICE_WEIGHT),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Price", fontWeight = FontWeight.SemiBold)
                    }

                    Box(
                        modifier = Modifier.weight(CoinListItemConstants.CHANGE_WEIGHT),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Change 24H", fontWeight = FontWeight.SemiBold)
                    }
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.topCoin) { coinUi ->
                        HorizontalDivider()
                        CoinListItem(
                            coinUi = coinUi, onClick = {
                                onAction(CoinListAction.OnCoinClick(coinUi))
                            }, modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            "More Coins",
                            modifier = Modifier.fillMaxWidth().clickable{
                                onAction(CoinListAction.OnMoreCoinsClick)
                            },
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }

    }
}