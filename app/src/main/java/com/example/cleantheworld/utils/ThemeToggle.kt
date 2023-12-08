package com.example.cleantheworld.utils

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    val isDarkTheme = mutableStateOf(false)
    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}