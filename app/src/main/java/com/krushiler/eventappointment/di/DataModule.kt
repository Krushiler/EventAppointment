package com.krushiler.eventappointment.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.krushiler.eventappointment.data.repository.AuthRepository
import org.koin.dsl.module

val dataModule = module {
    single { Firebase.auth }
    single { AuthRepository(get()) }
}