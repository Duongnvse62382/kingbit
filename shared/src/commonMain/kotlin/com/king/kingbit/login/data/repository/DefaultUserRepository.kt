package com.king.kingbit.login.data.repository

import com.king.kingbit.login.data.local.UserDao
import com.king.kingbit.login.data.local.model.UserEntity
import com.king.kingbit.login.domain.usecase.UserRepository
import kotlinx.coroutines.flow.Flow

class DefaultUserRepository(private val dao: UserDao) : UserRepository {
    override suspend fun register(username: String, password: String): Boolean {
        val exists = dao.getUserByUsername(username)
        if (exists != null) return false
        dao.insert(UserEntity(username = username, password = password))
        return true
    }

    override suspend fun login(username: String, password: String): Boolean {
        val user = dao.getUserByUsername(username) ?: return false
        return if (user.password == password) {
            dao.logoutUser()
            dao.setUserAuthenticated(username)
            true
        } else{
            false
        }
    }

    override suspend fun isUserExists(username: String): Boolean {
        return dao.isUserExists(username)
    }

    override suspend fun logoutUser(username: String) {
        dao.logoutUser()
    }

    override fun getUserAuthenticated():  Flow<UserEntity?> {
        return dao.getUserAuthenticated()
    }
}