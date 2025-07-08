package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import android.util.Log
import com.lab42.skillsclub.data.dto.request.UpdateStepPassReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.UpdateStepPassMapImpl
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class UpdateStepPassUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var updateStepPassMapImpl: UpdateStepPassMapImpl
) {
    suspend fun updatePass(data: UpdateStepPassReqDTO): AppState {
        Log.i("DATA!@#", data.toString())
        return try {
            updateStepPassMapImpl.mapResponse(apiService.updateStepPass(data))
        } catch (e: Exception) {
            Log.e("Error update pass", e.toString())
            return AppState.Error("400")
        }
    }
}