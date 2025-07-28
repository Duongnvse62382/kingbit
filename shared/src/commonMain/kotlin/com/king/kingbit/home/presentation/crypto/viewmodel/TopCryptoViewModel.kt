package com.king.kingbit.home.presentation.crypto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.kingbit.core.domain.onError
import com.king.kingbit.core.domain.onSuccess
import com.king.kingbit.home.domain.model.Coin
import com.king.kingbit.home.domain.usecase.CoinRepository
import com.king.kingbit.home.presentation.crypto.CoinListAction
import com.king.kingbit.home.presentation.crypto.CoinListMoreEvent
import com.king.kingbit.home.presentation.crypto.TopCoinState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TopCryptoViewModel(private val coinRepository: CoinRepository) : ViewModel() {
    private val _state = MutableStateFlow(TopCoinState())
    val state = _state.onStart {
        loadTopCryptoMarket()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TopCoinState()
    )

    private val _events = Channel<CoinListMoreEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        viewModelScope.launch {
            when (action) {
                is CoinListAction.OnCoinClick -> selectCoin(action.coinUi)
                CoinListAction.OnMoreCoinsClick -> {

                }
            }
        }
    }

    private suspend fun selectCoin(coinUi: Coin) {
        _events.send(CoinListMoreEvent.NavigateToCoinDetail(coinUi.id))
    }


    private fun loadTopCryptoMarket() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            coinRepository.getTopCoin(100).onSuccess { result ->
                _state.update {
                    it.copy(
                        isLoading = false, topCoin = result
                    )
                }

            }.onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }

                _events.send(CoinListMoreEvent.Error(error.name))
            }
        }
    }
}