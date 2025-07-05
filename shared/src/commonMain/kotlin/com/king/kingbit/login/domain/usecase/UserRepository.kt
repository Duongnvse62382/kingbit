package com.king.kingbit.login.domain.usecase

interface UserRepository {
    suspend fun register(username: String, password: String): Boolean
    suspend fun login(username: String, password: String): Boolean
    suspend fun isUserExists(username: String) : Boolean
}