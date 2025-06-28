package com.king.kingbit.di

import com.king.kingbit.login.data.local.AppDatabase
import com.king.kingbit.login.data.repository.UserRepository
import com.king.kingbit.login.presentation.LoginViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

//expect val platformModule: Module

val sharedModule = module {
    single { get<AppDatabase>().userDao() }
    single { UserRepository(get()) }

    viewModelOf(::LoginViewModel)
}