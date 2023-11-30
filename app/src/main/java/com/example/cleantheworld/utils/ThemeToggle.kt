package com.example.cleantheworld.utils

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    // This state will be observed by composables to react to theme changes.
    val isDarkTheme = mutableStateOf(false)

    // Call this method to toggle the theme
    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}