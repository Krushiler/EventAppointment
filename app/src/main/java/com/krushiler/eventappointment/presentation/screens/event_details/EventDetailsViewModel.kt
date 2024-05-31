package com.krushiler.eventappointment.presentation.screens.event_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushiler.eventappointment.data.model.Event
import com.krushiler.eventappointment.data.repository.EventsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val eventsRepository: EventsRepository
) : ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState get() = _loadingState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow get() = _errorFlow.asSharedFlow()

    private val _eventState = MutableStateFlow<Event?>(null)
    val eventState get() = _eventState.asStateFlow()

    private val eventId = savedStateHandle.get<String>("id") ?: ""

    init {
        loadEvent()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            _loadingState.update { true }
            try {
                val event = eventsRepository.getEvent(eventId)
                _eventState.update { event }
            } catch (e: Exception) {
                _errorFlow.emit(e.message ?: "Something went wrong")
            }
            _loadingState.update { false }
        }
    }
}