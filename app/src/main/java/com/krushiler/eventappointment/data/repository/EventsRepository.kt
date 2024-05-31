@file:Suppress("UNCHECKED_CAST")

package com.krushiler.eventappointment.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.krushiler.eventappointment.data.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class EventsRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val coroutineScope: CoroutineScope,
) {
    private val _events = MutableStateFlow<List<Event>>(listOf())
    val events get() = _events.asStateFlow()

    private suspend fun isMember(eventId: String): Boolean {
        return firestore.collection("EventAppointments")
            .where(Filter.equalTo("eventId", eventId))
            .where(Filter.equalTo("userId", auth.uid))
            .get().await().documents.isNotEmpty()
    }

    suspend fun getEvents(): List<Event> {
        val snapshot = firestore.collection("Events").get().await()
        val events = snapshot.documents.map { it.toEvent() }
        _events.update { events }
        return events
    }

    suspend fun getEvent(id: String): Event {
        val snapshot = firestore.collection("Events").document(id).get().await()
        return snapshot.toEvent()
    }

    suspend fun register(id: String) {
        val data = hashMapOf(
            "eventId" to id,
            "userId" to auth.uid,
        )
        firestore.collection("EventAppointments").add(data).await()
        coroutineScope.launch { getEvents() }

        _events.update {
            val items = it.toMutableList()
            val index = items.indexOfFirst { event -> event.id == id }
            if (index == -1) return@update it
            items[index] = items[index].copy(isMember = false)
            return@update items.toList()
        }
    }

    suspend fun unregister(id: String) {
        val documents = firestore.collection("EventAppointments")
            .where(Filter.equalTo("eventId", id))
            .where(Filter.equalTo("userId", auth.uid)).get().await()

        documents.forEach {
            firestore.collection("EventAppointments").document(it.id).delete().await()
        }

        _events.update {
            val items = it.toMutableList()
            val index = items.indexOfFirst { event -> event.id == id }
            if (index == -1) return@update it
            items[index] = items[index].copy(isMember = false)
            return@update items.toList()
        }
    }

    private suspend fun DocumentSnapshot.toEvent(): Event {
        val participants = get("participants") as? List<String> ?: listOf()
        return Event(
            id = id,
            date = getDate("date") ?: Calendar.getInstance().time,
            name = getString("name") ?: "",
            description = getString("description") ?: "",
            participants = participants,
            isMember = isMember(id),
            owner = getString("owner") ?: ""
        )
    }
}