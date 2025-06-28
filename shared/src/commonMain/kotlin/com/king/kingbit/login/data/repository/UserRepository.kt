package com.king.kingbit.login.data.repository

import com.king.kingbit.login.data.local.UserDao
import com.king.kingbit.login.data.local.model.UserEntity

class UserRepository(private val dao: UserDao) {
    suspend fun register(username: String, password: String): Boolean {
        val exists = dao.getUserByUsername(username)
        if (exists != null) return false
        dao.insert(UserEntity(username = username, password = password))
        return true
    }

    suspend fun login(username: String, password: String): Boolean {
        return dao.getUserByUsername(username)?.password == password
    }
}