package com.king.kingbit.login.data.local

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create() : RoomDatabase.Builder<AppDatabase>
}