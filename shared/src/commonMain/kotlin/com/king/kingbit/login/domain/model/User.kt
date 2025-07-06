package com.king.kingbit.login.domain.model

data class User(val id: Int = 0, val username: String = "", val password: String = "", val isAuthenticated : Boolean = false)