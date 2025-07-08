package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.GetSectionsResDto
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface SectionsMapInterface {
    suspend fun mapResponse(res: Response<GetSectionsResDto>): AppState
}

class SectionsMapImpl @Inject constructor(): SectionsMapInterface{
    override suspend fun mapResponse(res: Response<GetSectionsResDto>): AppState {
        return if (res.isSuccessful) {
            if (res.code() == 200) {
                AppState.Success(res.body()!!.data)
            } else {
                AppState.Error("400")
            }
        }else if (res.code() == 401) {
            AppState.Error("401")
        } else {
            AppState.Error("404")
        }
    }
}