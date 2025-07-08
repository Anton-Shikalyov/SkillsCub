package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import android.util.Log
import com.lab42.skillsclub.data.dto.request.UpdatePasswordReqDTO
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.UpdatePassMapImpl
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

public class UpdatePassUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var updatePassMapInterface: UpdatePassMapImpl
) {
    suspend fun updatePass(data: UpdatePasswordReqDTO): AppState {
        return try {
            updatePassMapInterface.mapResponse(apiService.updatePassword(data))
        } catch (e: Exception) {
            Log.e("Error update pass", e.toString())
            return AppState.Error("400")
        }
    }
}
