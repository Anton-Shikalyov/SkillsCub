package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.PositionItemDTO
import com.lab42.skillsclub.data.dto.response.PositionResponseDTO
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface PositionMapInterface {
    suspend fun mapResponse(res: Response<PositionResponseDTO>): AppState
}
class PositionMapImpl @Inject constructor() : PositionMapInterface {
    override suspend fun mapResponse(res: Response<PositionResponseDTO>): AppState {
        return if (res.isSuccessful) {
            AppState.Success(res.body()?.data)
        } else if (res.code() == 401) {
            AppState.Error("401")
        }else {
            AppState.Error("400")
        }
    }
}

data class ProfileDataState (
    val state: AppState,
    val data: List<PositionItemDTO>?
)