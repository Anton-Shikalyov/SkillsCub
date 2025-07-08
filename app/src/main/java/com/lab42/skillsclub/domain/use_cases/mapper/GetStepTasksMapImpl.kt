package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.GetStepTasksResDTO
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface StepTasksMapInterface {
    suspend fun mapResponse(res: Response<GetStepTasksResDTO>): AppState
}

class GetStepTasksMapImpl @Inject constructor(): StepTasksMapInterface {
    override suspend fun mapResponse(res: Response<GetStepTasksResDTO>): AppState {
        return if (res.isSuccessful) {
            if (res.code() == 200) {
                AppState.Success(res.body()!!.data)
            } else {
                AppState.Error("400")
            }
        } else {
            AppState.Error("404")
        }
    }
}