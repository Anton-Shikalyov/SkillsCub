package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.data.dto.response.ChoosePositionResDTO
import com.lab42.skillsclub.presentation.AppState
import retrofit2.Response
import javax.inject.Inject

interface UpdatePassMapInterface {
     fun mapResponse(res: Response<ChoosePositionResDTO>): AppState
}
class UpdatePassMapImpl @Inject constructor() : UpdatePassMapInterface {
    override  fun mapResponse(res: Response<ChoosePositionResDTO>): AppState {
        return if (res.isSuccessful) {
            if (res.code() == 200) {
                AppState.Success(res.body()!!.success)
            } else if (res.code() == 401) {
                AppState.Error("401")
            }else {
                AppState.Error("400")
            }
        } else {
            AppState.Error("404")
        }
    }
}