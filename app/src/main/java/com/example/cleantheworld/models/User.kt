package com.example.cleantheworld.models

data class User(
    val id: String = "",
    var name: String = "",
    val email: String = "",
    var age: Int = 0,
    var phone: String = "",
    var admin: Boolean = false,
)