package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.ChoosePositionResDTO
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response

interface SentPositionMapInterface {
    suspend fun mapResponse(res: Response<ChoosePositionResDTO>): AppState
}

class SentPositionMapImpl : SentPositionMapInterface {
    override suspend fun mapResponse(res: Response<ChoosePositionResDTO>): AppState {
        return if (res.isSuccessful) {
            if (res.body()?.success == "1") {
                AppState.SuccessAuth
            } else {
                AppState.Error("404")
            }
        } else {
            AppState.Error("400")
        }
    }
}
