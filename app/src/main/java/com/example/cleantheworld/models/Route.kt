package com.example.cleantheworld.models

data class Route(
    val id: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double,
    val endLongitude: Double,
    val routeDetails: String // Could be a JSON string or similar format
)