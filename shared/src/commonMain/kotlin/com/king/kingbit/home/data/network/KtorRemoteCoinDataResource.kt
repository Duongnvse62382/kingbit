package com.king.kingbit.home.data.network

import com.king.kingbit.core.data.safeCall
import com.king.kingbit.core.domain.DataError
import com.king.kingbit.core.domain.Result
import com.king.kingbit.home.data.dto.CoinDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://api.coingecko.com/"

class KtorRemoteCoinDataResource(
    private val httpClient: HttpClient
) : RemoteCoinDataResource {
    override suspend fun searchCoins(): Result<MutableList<CoinDto>, DataError.Remote> {
        return safeCall {
            httpClient.get(
                urlString = "$BASE_URL/api/v3/coins/markets"
            ) {
                parameter("vs_currency", "usd")
                parameter("order", "market_cap_desc")
                parameter("per_page", 20)
                parameter("page", 1)
                parameter("sparkline", false)
            }
        }
    }

    override suspend fun getCoinDetails(id: String): Result<CoinDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/api/v3/coins/$id")
        }
    }
}