package com.king.kingbit.home.presentation

import com.king.kingbit.home.domain.model.Coin

data class TopCoinState(
    val topCoin: MutableList<Coin> = mutableListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
