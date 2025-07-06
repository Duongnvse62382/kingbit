package com.king.kingbit.home.data.repository

import com.king.kingbit.core.domain.DataError
import com.king.kingbit.core.domain.Result
import com.king.kingbit.core.domain.map
import com.king.kingbit.home.data.mappers.toCoin
import com.king.kingbit.home.data.network.RemoteCoinDataResource
import com.king.kingbit.home.domain.model.Coin
import com.king.kingbit.home.domain.usecase.CoinRepository

class CoinRepositoryImp(private val remoteCoinDataResource: RemoteCoinDataResource) : CoinRepository {
    override suspend fun getTopCoin(): Result<MutableList<Coin>, DataError.Remote> {
        return remoteCoinDataResource.searchCoins().map { dtoList ->
            dtoList.map { it.toCoin() }.toMutableList()
        }
    }

    override suspend fun getCoinDetail(id: String): Result<Coin, DataError.Remote> {
        return remoteCoinDataResource.getCoinDetails(id).map {
            it.toCoin()
        }
    }
}