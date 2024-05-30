@file:Suppress("UNCHECKED_CAST")

package com.krushiler.eventappointment.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.krushiler.eventappointment.data.model.Event
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class EventsRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getEvents(): List<Event> {
        val snapshot = firestore.collection("Events").get().await()
        return snapshot.documents.map { it.toEvent(auth.currentUser) }
    }

    suspend fun getEvent(id: String): Event {
        val snapshot = firestore.collection("Events").document(id).get().await()
        return snapshot.toEvent(auth.currentUser)
    }
}

fun DocumentSnapshot.toEvent(user: FirebaseUser?): Event {
    val participants = get("participants") as? List<String> ?: listOf()
    return Event(
        id = id,
        date = getDate("date") ?: Calendar.getInstance().time,
        name = getString("name") ?: "",
        description = getString("description") ?: "",
        participants = participants,
        isMember = participants.contains(user?.uid),
        owner = getString("owner") ?: ""
    )
}