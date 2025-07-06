package com.king.kingbit.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.kingbit.core.domain.onError
import com.king.kingbit.core.domain.onSuccess
import com.king.kingbit.home.domain.usecase.CoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(coinRepository: CoinRepository) : ViewModel() {
    private val _state = MutableStateFlow(TopCoinState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            coinRepository.getTopCoin().onSuccess { result ->
                _state.update {
                    it.copy(
                        topCoin = result
                    )
                }

            }.onError { error ->
                _state.update {
                    it.copy(
                        topCoin = mutableListOf(), errorMessage = error.name
                    )
                }
            }
        }
    }
}