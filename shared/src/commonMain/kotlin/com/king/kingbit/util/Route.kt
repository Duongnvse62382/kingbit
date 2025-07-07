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
    data object Home : Route

    @Serializable
    data object Email : Route

    @Serializable
    data object Settings : Route
}