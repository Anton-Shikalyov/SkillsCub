package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.ProgramsByPosReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.GetProgramsByPosInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class GetProgramsByPosUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var getProgramsByPosInterface: GetProgramsByPosInterface
    ) {
    suspend fun getPrograms(data: ProgramsByPosReqDTO) : AppState {
        return try {
            val response = apiService.getProgramsByPosition(data)
            getProgramsByPosInterface.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}