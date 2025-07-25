package com.king.kingbit.login.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory( private val context: Context) {
    actual fun create(): RoomDatabase.Builder<AppDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(AppDatabase.DB_NAME)
        return Room.databaseBuilder(
                    context = appContext,
                    name = dbFile.absolutePath,
                    klass = AppDatabase::class.java
                ).fallbackToDestructiveMigration(false)
    }
}