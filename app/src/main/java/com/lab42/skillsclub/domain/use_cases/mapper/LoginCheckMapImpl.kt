package com.lab42.skillsclub.domain.use_cases.mapper

import android.util.Log
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.response.LoginCheckResponseDTO
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import retrofit2.Response
import javax.inject.Inject

interface LoginCheckMapInterface {
    suspend fun mapResponse(res: Response<LoginCheckResponseDTO>): StateBundle
}
class LoginCheckMapImpl @Inject constructor(): LoginCheckMapInterface {
    override suspend fun mapResponse(res: Response<LoginCheckResponseDTO>): StateBundle {
        return if (res.isSuccessful) {
            if (res.body()?.success == 1) {
                when (res.body()?.data?.status) {
                    "created" -> StateBundle(AppState.Success(null),  R.id.action_LoginFragment_to_CreatePassFragment, null)
                    "active" ->  StateBundle(AppState.Success(null),  R.id.action_LoginFragment_to_PasswordFragment, null)
                    else -> StateBundle(AppState.Error("400"),  null, null)
                }
            } else {
                StateBundle(AppState.Error("404"), null, null)
            }
        } else {
            StateBundle(AppState.Error("400"), null, null)
        }
    }
}