package com.krushiler.eventappointment.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.krushiler.eventappointment.data.model.AppException
import com.krushiler.eventappointment.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState get() = _errorState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState get() = _loadingState.asStateFlow()

    fun inputChanged() {
        _errorState.update { null }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loadingState.update { true }
            try {
                authRepository.login(login, password)
            } catch (e: AppException) {
                _errorState.update { e.message }
            }
            _loadingState.update { false }
        }
    }

    fun signInWithAuthCredential(credential: AuthCredential) {
        viewModelScope.launch {
            _loadingState.update { true }
            try {
                authRepository.signInWithAuthCredential(credential)
            } catch (e: AppException) {
                _errorState.update { e.message }
            }
            _loadingState.update { false }
        }
    }

    fun register(login: String, password: String) {
        viewModelScope.launch {
            _loadingState.update { true }
            try {
                authRepository.register(login, password)
            } catch (e: AppException) {
                _errorState.update { e.message }
            }
            _loadingState.update { false }
        }
    }
}