package com.example.cleantheworld.models

data class Notification(
    val id: String,
    val userId: String, // ID of the user receiving the notification
    val message: String,
    val timestamp: Long // Use Unix timestamp for simplicity
)
