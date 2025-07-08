package com.lab42.skillsclub.domain.use_cases.mapper

import android.util.Log
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.dto.response.PassSuccessResponseDTO
import com.lab42.skillsclub.data.dto.response.PositionItemDTO
import com.lab42.skillsclub.data.retrofit.ApiService
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import retrofit2.Response
import javax.inject.Inject

interface TokenAuthMapInterface {
    suspend fun mapResponse(res: Response<PassSuccessResponseDTO>): StateBundle
}
class TokenAuthMapImpl @Inject constructor(
    private val dbProfileUseCase: DBProfileUseCase,
    private val apiService: ApiService
) : TokenAuthMapInterface {
    override suspend fun mapResponse(res: Response<PassSuccessResponseDTO>): StateBundle {
        Log.i("auth12312312", res.body().toString())

        return if (res.isSuccessful) {
            dbProfileUseCase.saveAuthData(res.body()?.data!!.profile)
                if (res.body()?.data!!.profile.currentPosition > 0) {
                    val pos: List<PositionItemDTO>? = apiService.getPositions().body()?.data
                    for(item in pos!!) {
                        if (res.body()?.data!!.profile.currentPosition == item.id) {
                            dbProfileUseCase.updateCurrentPositionName(item.name)
                        }
                    }
                    StateBundle(AppState.SuccessAuth, null, null)
                } else {
                    StateBundle(AppState.Error("nullPosition"), R.id.PositionFragment, null)
                }
            } else {
                StateBundle(AppState.Error(res.code().toString()), null, null)
            }
    }
}