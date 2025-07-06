package com.king.kingbit.login.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.king.kingbit.login.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM UserEntity WHERE username = :username)")
    suspend fun isUserExists(username: String): Boolean

    @Query("UPDATE UserEntity SET isAuthenticated = 1 WHERE username = :username")
    suspend fun setUserAuthenticated(username: String)

    @Query("UPDATE UserEntity SET isAuthenticated = 0")
    suspend fun logoutUser()

    @Query("SELECT * FROM UserEntity WHERE isAuthenticated = 1 LIMIT 1")
    fun getUserAuthenticated(): Flow<UserEntity?>
}