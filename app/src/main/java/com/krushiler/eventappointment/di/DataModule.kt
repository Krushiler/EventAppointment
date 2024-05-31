package com.krushiler.eventappointment.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.krushiler.eventappointment.data.repository.AuthRepository
import com.krushiler.eventappointment.data.repository.EventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dataModule = module {
    factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single { Firebase.auth }
    single { Firebase.firestore }
    single { AuthRepository(get()) }
    single { EventsRepository(get(), get(), get()) }
}