package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.TokenAuthMapInterface
import com.lab42.skillsclub.presentation.StateBundle
import javax.inject.Inject

class TokenAuthUseCase  @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var tokenMap: TokenAuthMapInterface
) {
    suspend fun tokenAuth() : StateBundle {
        return tokenMap.mapResponse(apiService.tokenAuth())
    }
}