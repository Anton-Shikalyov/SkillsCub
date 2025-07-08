package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.GetSectionsReqDTO
import com.lab42.skillsclub.data.dto.request.GetStepsReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.StepsMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class GetStepsUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var stepsMapInterface: StepsMapInterface

    ) {
    suspend fun getSteps(data: GetStepsReqDTO) : AppState {
        return try {
            val response = apiService.getSteps(data)
            stepsMapInterface.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}