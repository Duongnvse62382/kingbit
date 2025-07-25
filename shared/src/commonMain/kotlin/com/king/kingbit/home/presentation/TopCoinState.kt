package com.king.kingbit.home.presentation

import androidx.compose.runtime.Immutable
import com.king.kingbit.home.domain.model.Coin

@Immutable
data class TopCoinState(
    val topCoin: MutableList<Coin> = mutableListOf(),
    val isLoading: Boolean = false,
)
