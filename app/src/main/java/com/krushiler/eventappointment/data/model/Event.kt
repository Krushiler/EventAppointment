package com.krushiler.eventappointment.data.model

import java.util.Date

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val participants: List<String>,
    val date: Date,
    val isMember: Boolean,
    val owner: String
)