package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.GetHelpMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class GetHelpUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var getHelpMapInterface: GetHelpMapInterface
) {
    suspend fun getHelp() : AppState {
        return try {
            val response = apiService.getHelp()
            getHelpMapInterface.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}