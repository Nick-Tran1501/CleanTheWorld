package com.example.cleantheworld.models

enum class DirtyLevel {
    HIGH,
    MEDIUM,
    LOW,
    CLEANED
}
fun parseDirtyLevel(value: String): DirtyLevel {
    return try {
        enumValueOf<DirtyLevel>(value)
    } catch (e: IllegalArgumentException) {
        DirtyLevel.CLEANED // Return a default value or handle the error as needed
    }
}