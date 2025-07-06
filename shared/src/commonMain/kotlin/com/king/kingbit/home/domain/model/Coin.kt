package com.king.kingbit.home.domain.model


data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: Double,
    val marketCap: Long,
    val marketCapRank: Int,
)
