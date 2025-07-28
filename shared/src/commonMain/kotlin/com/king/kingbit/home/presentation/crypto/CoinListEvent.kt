package com.king.kingbit.home.presentation.crypto

sealed interface CoinListEvent {
    data class Error(val error: String): CoinListEvent
    data class NavigateToCoinDetail(val coinId: String): CoinListEvent
    data object NavigateToMoreCoins: CoinListEvent
}

sealed interface CoinListMoreEvent {
    data class Error(val error: String): CoinListMoreEvent
    data class NavigateToCoinDetail(val coinId: String): CoinListMoreEvent
}