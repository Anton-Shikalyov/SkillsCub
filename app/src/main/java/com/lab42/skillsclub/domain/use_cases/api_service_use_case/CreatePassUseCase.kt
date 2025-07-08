package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.LoginAndPassReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.CreatePassMapInterface
import com.lab42.skillsclub.presentation.StateBundle
import javax.inject.Inject

class CreatePassUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private val createPassMap: CreatePassMapInterface
) {
    suspend fun auth(data: LoginAndPassReqDTO): StateBundle {

        return createPassMap.mapResponse(apiService.auth(data))
    }
}