package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.GetStepByIdReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.GetStepByIdMapImpl
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class GetStepByIdUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var getStepByIdMapImpl: GetStepByIdMapImpl
) {
    suspend fun getStepById(data: GetStepByIdReqDTO) : AppState {
        return try {
            val response = apiService.getStepBuId(data)
            getStepByIdMapImpl.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}