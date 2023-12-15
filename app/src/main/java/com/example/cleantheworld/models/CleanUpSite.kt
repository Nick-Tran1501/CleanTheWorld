package com.example.cleantheworld.models

data class CleanUpSite(
    val id: String = "",
    val name: String = "",
    val level: DirtyLevel = DirtyLevel.CLEANED,
    val shortDescription: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val participantIds: List<String> = listOf(), // User IDs of participants
    val adminId: String = ""// User ID of the admin
)