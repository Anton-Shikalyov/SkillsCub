package com.lab42.skillsclub.presentation

sealed class AppState {
    data class Success<T>(val data: T) : AppState()
    data object SuccessAuth : AppState()
    data object Loading : AppState()
    data object Splash : AppState()
    data object ToLogin : AppState()
    data class Error(val code: String) : AppState()
}
