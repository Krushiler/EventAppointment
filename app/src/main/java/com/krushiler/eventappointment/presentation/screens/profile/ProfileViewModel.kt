package com.krushiler.eventappointment.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushiler.eventappointment.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val _nameState = MutableStateFlow("")
    val nameState = _nameState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.user.collect { user ->
                _nameState.update { user?.displayName ?: "" }
            }
        }
    }

    fun saveProfile(name: String) {
        viewModelScope.launch {
            _loadingState.update { true }
            try {
                authRepository.changeProfile(name)
            } catch (e: Exception) {
                _errorFlow.emit(e.message ?: "Something went wrong")
            }
            _loadingState.update { false }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}