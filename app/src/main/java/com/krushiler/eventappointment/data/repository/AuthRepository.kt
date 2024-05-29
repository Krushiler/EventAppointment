package com.krushiler.eventappointment.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.krushiler.eventappointment.data.model.AppException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth) {
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user get() = _user.asStateFlow()

    init {
        auth.addAuthStateListener { authState ->
            _user.update { authState.currentUser }
        }
    }

    suspend fun login(email: String, password: String) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user == null) {
                throw AppException("Authentication failed")
            }
        } catch (e: Exception) {
            throw AppException("Something went wrong")
        }
    }

    suspend fun register(email: String, password: String) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user == null) {
                throw AppException("Authentication failed")
            }
        } catch (e: Exception) {
            throw AppException("Something went wrong")
        }
    }
}