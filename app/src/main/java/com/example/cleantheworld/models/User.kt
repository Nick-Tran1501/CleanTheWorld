package com.example.cleantheworld.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val age: Int,
    val phone: String,
    var joinedSiteIds: List<String>// IDs of sites the user has joined
)


