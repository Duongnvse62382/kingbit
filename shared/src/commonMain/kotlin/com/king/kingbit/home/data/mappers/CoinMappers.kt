package com.king.kingbit.home.data.mappers

import com.king.kingbit.home.data.dto.CoinDto
import com.king.kingbit.home.domain.model.Coin

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = imageUrl,
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank,
    )
}