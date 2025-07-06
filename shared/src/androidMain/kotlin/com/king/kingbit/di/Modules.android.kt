package com.king.kingbit.di


import com.king.kingbit.login.data.local.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.HttpClientEngine
import org.koin.android.ext.koin.androidApplication


actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> {OkHttp.create()}
        single { DatabaseFactory(androidApplication()) }
    }
