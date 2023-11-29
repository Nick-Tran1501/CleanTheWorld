package com.example.cleantheworld.models

data class CleanUpSite(
    val id: String,
    val name: String,
    val level: DirtyLevel,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val participantIds: List<String>, // User IDs of participants
    val adminId: String // User ID of the admin
)