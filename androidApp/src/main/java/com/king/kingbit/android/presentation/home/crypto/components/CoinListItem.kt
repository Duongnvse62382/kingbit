package com.king.kingbit.android.presentation.home.crypto.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.king.kingbit.android.presentation.home.crypto.constants.CoinListItemConstants
import com.king.kingbit.home.domain.model.Coin


@Composable
fun CoinListItem(
    coinUi: Coin,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.weight(CoinListItemConstants.COIN_WEIGHT),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = coinUi.imageUrl,
                    contentDescription = coinUi.name,
                    modifier = Modifier.size(40.dp)
                )
                Column {
                    Text(
                        text = coinUi.symbol.uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = coinUi.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }

        Box(
            modifier = Modifier.weight(CoinListItemConstants.PRICE_WEIGHT),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "$ ${coinUi.currentPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Box(
            modifier = Modifier.weight(CoinListItemConstants.CHANGE_WEIGHT),
            contentAlignment = Alignment.Center
        ) {
            Text("Change 24H", fontWeight = FontWeight.SemiBold)
        }

    }
}
