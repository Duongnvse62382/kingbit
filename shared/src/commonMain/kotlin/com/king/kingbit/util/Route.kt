package com.king.kingbit.util

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    //Patent
    @Serializable
    data object  Splash : Route

    @Serializable
    data object Login: Route

    @Serializable
    data object Register: Route

    @Serializable
    data object Main : Route

    //Bottom Nav
    @Serializable
    data object CoinList : Route

    @Serializable
    data class CoinDetail(val coinId: String = "") : Route

    @Serializable
    data object CoinTop : Route

    @Serializable
    data object Game : Route

    @Serializable
    data class GameDetail(val gameId: String = "") : Route

    @Serializable
    data object Settings : Route

}