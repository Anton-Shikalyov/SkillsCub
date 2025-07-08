package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.GetSectionsReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.SectionsMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class GetSectionsUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var sectionsMapInterface: SectionsMapInterface
) {
    suspend fun getSections(data: GetSectionsReqDTO) : AppState {
        return try {
            val response = apiService.getSections(data)
            sectionsMapInterface.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}