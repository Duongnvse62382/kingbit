package com.king.kingbit.home.presentation

sealed interface CoinListEvent {
    data class Error(val error: String): CoinListEvent
    data class NavigateToCoinDetail(val coinId: String): CoinListEvent
    data object NavigateToMoreCoins: CoinListEvent
}