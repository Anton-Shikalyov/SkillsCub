package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.PositionMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class PositionUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var profileMap: PositionMapInterface
) {
    suspend fun getPositions(): AppState {
        return profileMap.mapResponse(apiService.getPositions())

    }

}