package com.example.cleantheworld.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val passwordHash: String, // Store hashed passwords only for security
    val joinedSiteIds: List<String> // IDs of sites the user has joined
)


