package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.GetStepByIdResDto
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface GetStepByIdMapInterface {
    suspend fun mapResponse(res: Response<GetStepByIdResDto>): AppState
}
class GetStepByIdMapImpl @Inject constructor() : GetStepByIdMapInterface {
    override suspend fun mapResponse(res: Response<GetStepByIdResDto>): AppState {
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