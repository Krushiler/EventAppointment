package com.krushiler.eventappointment.di

import com.krushiler.eventappointment.presentation.screens.event_details.EventDetailsViewModel
import com.krushiler.eventappointment.presentation.screens.events.EventsViewModel
import com.krushiler.eventappointment.presentation.screens.login.LoginViewModel
import com.krushiler.eventappointment.presentation.screens.main.MainViewModel
import com.krushiler.eventappointment.presentation.screens.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EventsViewModel(get()) }
    viewModel { EventDetailsViewModel(get(), get()) }
}