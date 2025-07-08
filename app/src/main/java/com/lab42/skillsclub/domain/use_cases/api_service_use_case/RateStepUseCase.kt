package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.RateStepReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.RateStepMapImpl
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class RateStepUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private val rateStepMapImpl: RateStepMapImpl
) {
    suspend fun rateStep(feedback: RateStepReqDTO): AppState {
        return try {
            val response = apiService.rateStep(feedback)
            rateStepMapImpl.mapResponse(response)
        } catch (e: Exception) {

            AppState.Error("404")
        }
    }
}