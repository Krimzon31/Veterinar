package com.example.pet_pawtrol.adapters

data class AppointmentModel(
    val id: Int?,
    val date: String,
    val time: String,
    val nicknamePets: String,
    val veterinarName: String?,
    val user_id: Int
)
