package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.LogOutResDto
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface RateStepMapInterface {
    suspend fun mapResponse(res: Response<LogOutResDto>): AppState
}
class RateStepMapImpl @Inject constructor() : RateStepMapInterface {
    override suspend fun mapResponse(res: Response<LogOutResDto>): AppState {
        return if (res.isSuccessful) {
            if (res.body()?.success == 1) {
                AppState.Success("")
            } else {
                AppState.Error("404")
            }
        } else if (res.code() == 401) {
            AppState.Error("401")
        } else {
            AppState.Error("400")
        }
    }
}