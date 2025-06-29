package com.king.kingbit.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.king.kingbit.core.data.HttpClientFactory
import com.king.kingbit.login.data.local.AppDatabase
import com.king.kingbit.login.data.local.DatabaseFactory
import com.king.kingbit.login.data.repository.DefaultUserRepository
import com.king.kingbit.login.domain.usecase.UserRepository
import com.king.kingbit.login.presentation.LoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind


expect val platformModule : Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().userDao() }
    singleOf(::DefaultUserRepository) bind UserRepository::class
    viewModelOf(::LoginViewModel)
}