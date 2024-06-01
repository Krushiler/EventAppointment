package com.krushiler.eventappointment.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.krushiler.eventappointment.data.model.AppException
import com.krushiler.eventappointment.data.model.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth) {
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user get() = _user.asStateFlow()

    private val _authState = MutableStateFlow(AuthState.Loading)
    val authState get() = _authState.asStateFlow()

    init {
        auth.addAuthStateListener { authState ->
            _user.update { authState.currentUser }
            _authState.update { if (authState.currentUser != null) AuthState.SignedIn else AuthState.NotSignedIn }
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun changeProfile(name: String) {
        auth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().apply {
                displayName = name
            }.build()
        )?.await()
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

    suspend fun signInWithAuthCredential(credential: AuthCredential) {
        try {
            val result = auth.signInWithCredential(credential).await()
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
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().apply {
                    displayName = email
                }.build()
            )?.await()
        } catch (e: Exception) {
            throw AppException("Something went wrong")
        }
    }
}