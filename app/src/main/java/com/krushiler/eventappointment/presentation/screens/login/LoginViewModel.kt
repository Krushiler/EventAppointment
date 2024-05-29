package com.krushiler.eventappointment.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushiler.eventappointment.data.model.AppException
import com.krushiler.eventappointment.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState get() = _errorState.asStateFlow()

    fun inputChanged() {
        _errorState.update { null }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.login(login, password)
            } catch (e: AppException) {
                _errorState.update { e.message }
            }
        }
    }

    fun register(login: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.register(login, password)
            } catch (e: AppException) {
                _errorState.update { e.message }
            }
        }
    }
}