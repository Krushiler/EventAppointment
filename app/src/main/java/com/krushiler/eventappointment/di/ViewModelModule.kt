package com.krushiler.eventappointment.di

import com.krushiler.eventappointment.presentation.screens.login.LoginViewModel
import com.krushiler.eventappointment.presentation.screens.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
}