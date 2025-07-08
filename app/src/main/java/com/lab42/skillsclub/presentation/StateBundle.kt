package com.lab42.skillsclub.presentation

import android.os.Bundle

data class RouteBundle (
    val route: Int,
    val bundle: Bundle?
)

data class StateBundle (
    val state: AppState,
    val route: Int?,
    val bundle: Bundle?
)