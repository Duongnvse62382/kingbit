package com.king.kingbit.android

import android.app.Application
import com.king.kingbit.di.initKoin
import org.koin.android.ext.koin.androidContext

class KingBitApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@KingBitApplication)
        }
    }
}