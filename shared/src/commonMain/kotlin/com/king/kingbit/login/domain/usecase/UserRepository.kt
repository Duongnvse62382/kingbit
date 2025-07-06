package com.king.kingbit.login.domain.usecase

import com.king.kingbit.login.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun register(username: String, password: String): Boolean
    suspend fun login(username: String, password: String): Boolean
    suspend fun isUserExists(username: String) : Boolean
    suspend fun logoutUser(username: String)
    fun getUserAuthenticated() : Flow<UserEntity?>
}