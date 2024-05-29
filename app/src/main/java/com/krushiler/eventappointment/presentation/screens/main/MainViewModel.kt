package com.krushiler.eventappointment.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushiler.eventappointment.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow(false)
    val authState = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.user.collect { user ->
                _authState.update { user != null }
            }
        }
    }
}