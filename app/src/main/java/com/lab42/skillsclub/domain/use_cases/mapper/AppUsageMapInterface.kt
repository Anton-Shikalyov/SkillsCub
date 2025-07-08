package com.lab42.skillsclub.domain.use_cases.mapper

import android.util.Log
import com.lab42.skillsclub.data.dto.response.LogOutResDto
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface AppUsageMapInterface {
    suspend fun mapResponse(res: Response<LogOutResDto>): AppState

}

class AppUsageMapImpl @Inject constructor(): AppUsageMapInterface {
    override suspend fun mapResponse(res: Response<LogOutResDto>): AppState {
        return if (res.isSuccessful) {
            AppState.Success(null)
        }
        else {
            if (res.code() == 401) {
                AppState.Error("401")
            } else {
                AppState.Error("401")
            }
        }
    }

}