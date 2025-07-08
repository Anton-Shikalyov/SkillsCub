package com.lab42.skillsclub.domain.use_cases.mapper

import HelloResDTO
import com.lab42.skillsclub.R
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import retrofit2.Response

interface HelloMapInterface {
    suspend fun mapResponse(res: Response<HelloResDTO>): StateBundle
}

class HelloMapImpl: HelloMapInterface {
    override suspend fun mapResponse(res: Response<HelloResDTO>): StateBundle {

        return if (res.isSuccessful) {
            if (res.code() == 200) {
                StateBundle(AppState.Success(res.body()), R.id.LoginFragment, null)
            } else {
                StateBundle(AppState.Error("400"),null, null)
            }
        } else {
            StateBundle(AppState.Error("404"),null, null)
        }
    }
}