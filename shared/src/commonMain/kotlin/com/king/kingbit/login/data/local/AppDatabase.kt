package com.king.kingbit.login.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.king.kingbit.login.data.local.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DB_NAME = "king_bit.db"
    }
}