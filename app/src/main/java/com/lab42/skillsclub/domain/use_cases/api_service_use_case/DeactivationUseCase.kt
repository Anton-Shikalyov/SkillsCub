package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.DeactivationMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class DeactivationUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var deactivationMapInterface: DeactivationMapInterface,) {
    suspend fun sendDeactivationReq() : AppState {
        return try {
            val response = apiService.deactivationAccount()
            deactivationMapInterface.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}