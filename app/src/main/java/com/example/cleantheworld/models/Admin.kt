package com.example.cleantheworld.models

data class Admin(
    val user: User,
    val managedSiteIds: List<String> // IDs of sites the admin manages
)