package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.ChoosePositionReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.SentPositionMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class SentPositionUseCase  @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var profileMap: SentPositionMapInterface
) {
    suspend fun sendPosition(data: ChoosePositionReqDTO): AppState {
        return profileMap.mapResponse(apiService.sendSelectedPositions(data))
    }
}