package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.GetHelpResDTO
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface GetHelpMapInterface {
    suspend fun mapResponse(res: Response<GetHelpResDTO>): AppState
}
class GetHelpMapImpl @Inject constructor() : GetHelpMapInterface {

    override suspend fun mapResponse(res: Response<GetHelpResDTO>): AppState {
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