package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.dto.request.LoginCheckReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.LoginCheckMapInterface
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import javax.inject.Inject

class LoginCheckUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private val loginCheckMapInterface: LoginCheckMapInterface
) {
    suspend fun getLoginCheck(login: String): StateBundle {
        return try {
            val response = apiService.getLoginCheck(LoginCheckReqDTO(login))
            loginCheckMapInterface.mapResponse(response)
        } catch (e: Exception) {
            StateBundle(AppState.Error("404"), null, null)
        }
    }
}