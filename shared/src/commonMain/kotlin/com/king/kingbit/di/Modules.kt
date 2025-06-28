package com.king.kingbit.di

import com.king.kingbit.core.data.HttpClientFactory
import com.king.kingbit.login.data.local.AppDatabase
import com.king.kingbit.login.domain.usecase.UserRepository
import com.king.kingbit.login.presentation.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf


val sharedModule = module {
    single { get<AppDatabase>().userDao() }
    single { HttpClientFactory.create(get()) }

    singleOf(::DefaultUserRepository).bind<UserRepository>()
    viewModelOf(::LoginViewModel)
}