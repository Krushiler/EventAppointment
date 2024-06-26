package com.krushiler.eventappointment.presentation.screens.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushiler.eventappointment.data.model.Event
import com.krushiler.eventappointment.data.repository.EventsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventsViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow get() = _errorFlow.asSharedFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState get() = _loadingState.asStateFlow()

    private val _eventsState = MutableStateFlow(listOf<Event>())
    val eventsState get() = _eventsState.asStateFlow()

    init {
        loadEvents()
        watchEvents()
    }

    private fun watchEvents() {
        viewModelScope.launch {
            eventsRepository.events.collectLatest { events ->
                _eventsState.update { events }
            }
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _loadingState.update { true }
            try {
                eventsRepository.getEvents()
            } catch (e: Exception) {
                _errorFlow.emit(e.message ?: "Something went wrong")
            }
            _loadingState.update { false }
        }
    }
}