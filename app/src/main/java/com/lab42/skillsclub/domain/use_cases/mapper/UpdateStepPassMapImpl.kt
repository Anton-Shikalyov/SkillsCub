package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.LogOutResDto
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface UpdateStepPassMapInterface {
    fun mapResponse(res: Response<LogOutResDto>): AppState
}
class UpdateStepPassMapImpl @Inject constructor() : UpdateStepPassMapInterface {
    override fun mapResponse(res: Response<LogOutResDto>): AppState {
        return if (res.isSuccessful) {
            if (res.code() == 200) {
                AppState.Success(res.body()!!.success)

            } else {
                AppState.Error("400")
            }
        } else {
            AppState.Error("404")
        }
    }
}