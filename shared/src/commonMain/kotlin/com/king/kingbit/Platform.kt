package com.king.kingbit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform