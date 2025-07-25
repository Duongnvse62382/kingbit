package com.king.kingbit.home.presentation

import com.king.kingbit.home.domain.model.Coin

sealed interface CoinListAction {
    data class OnCoinClick(val coinUi: Coin): CoinListAction
    data object OnMoreCoinsClick: CoinListAction
}