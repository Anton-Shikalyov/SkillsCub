package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.HelloMapInterface
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import javax.inject.Inject

class HelloUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private val helloMapInterface: HelloMapInterface
) {
    suspend fun getHelloUseCase(): StateBundle {
        return try {
            val response = apiService.getHello()
            helloMapInterface.mapResponse(response)
        } catch (e: Exception) {
            StateBundle(AppState.Error("400"), null, null)
        }
    }

}