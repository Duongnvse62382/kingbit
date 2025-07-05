package com.king.kingbit.util

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object  Splash : Route

    @Serializable
    data object Login: Route

    @Serializable
    data object Register: Route

    @Serializable
    data object Home : Route
}