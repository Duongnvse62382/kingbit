package com.king.kingbit.home.domain.usecase

import com.king.kingbit.core.domain.DataError
import com.king.kingbit.core.domain.Result
import com.king.kingbit.home.domain.model.Coin

interface CoinRepository {
    suspend fun getTopCoin() : Result<MutableList<Coin>, DataError.Remote>
    suspend fun getCoinDetail(id : String) : Result<Coin, DataError.Remote>
}