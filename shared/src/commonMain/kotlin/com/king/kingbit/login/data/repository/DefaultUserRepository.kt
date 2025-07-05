package com.king.kingbit.login.data.repository

import com.king.kingbit.login.data.local.UserDao
import com.king.kingbit.login.data.local.model.UserEntity
import com.king.kingbit.login.domain.usecase.UserRepository

class DefaultUserRepository (private val dao: UserDao) : UserRepository {
    override suspend fun register(username: String, password: String): Boolean {
        val exists = dao.getUserByUsername(username)
        if (exists != null) return false
        dao.insert(UserEntity(username = username, password = password))
        return true
    }

    override suspend fun login(username: String, password: String): Boolean {
        return dao.getUserByUsername(username)?.password == password
    }

    override suspend fun isUserExists(username: String): Boolean {
        return dao.isUserExists(username)
    }
}