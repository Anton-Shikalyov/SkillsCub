package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.AppUsageReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.AppUsageMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class AppUsageUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private val appUsageMapInterface: AppUsageMapInterface
) {
    suspend fun updateAppUsage(data: AppUsageReqDTO) : AppState {
        return try {
            val response = apiService.setAppUsage(data)
            appUsageMapInterface.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}