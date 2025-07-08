package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.GetStepTasksReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.GetStepTasksMapImpl
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class GetStepTasksUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var getStepTasksMapImpl: GetStepTasksMapImpl
) {
    suspend fun getStepTasks(data: GetStepTasksReqDTO) : AppState {
        return try {
            val response = apiService.getStepTasks(data)
            getStepTasksMapImpl.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}