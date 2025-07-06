package com.king.kingbit.login.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.king.kingbit.login.data.local.model.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM UserEntity WHERE username = :username)")
    suspend fun isUserExists(username: String): Boolean
}