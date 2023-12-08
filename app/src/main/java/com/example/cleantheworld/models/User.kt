package com.example.cleantheworld.models

data class User(
    val id: String = "",
    var name: String = "",
    val email: String = "",
    var age: Int = 0,
    var phone: String = "",
    var joinedSiteIds: List<String> = listOf(),// IDs of sites the user has joined
    var admin: Boolean = false,
)


