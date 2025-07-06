package com.king.kingbit.home.data.network

import com.king.kingbit.core.domain.DataError
import com.king.kingbit.core.domain.Result
import com.king.kingbit.home.data.dto.CoinDto

interface RemoteCoinDataResource {
    suspend fun searchCoins(): Result<MutableList<CoinDto>, DataError.Remote>

    suspend fun getCoinDetails(id: String): Result<CoinDto, DataError.Remote>
}