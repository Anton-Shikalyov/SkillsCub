package com.lab42.skillsclub.domain.use_cases.mapper

import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.SharedPrefInterface
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.dto.response.PassSuccessResponseDTO
import com.lab42.skillsclub.data.dto.response.PositionItemDTO
import com.lab42.skillsclub.data.retrofit.ApiService
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import retrofit2.Response
import javax.inject.Inject

interface CreatePassMapInterface {
    suspend fun mapResponse(res: Response<PassSuccessResponseDTO>): StateBundle
}
class CreatePassMapImpl @Inject constructor(
    private val dbProfileUseCase: DBProfileUseCase,
    private val sharedPrefInterface: SharedPrefInterface,
    private val apiService: ApiService
): CreatePassMapInterface {
    override suspend fun mapResponse(res: Response<PassSuccessResponseDTO>): StateBundle {
        return if (res.isSuccessful) {
            sharedPrefInterface.setToken(res.body()?.data!!.token)
            dbProfileUseCase.saveAuthData(res.body()?.data!!.profile)
            if (res.body()?.data!!.profile.currentPosition > 0) {
                val pos: List<PositionItemDTO>? = apiService.getPositions().body()?.data

                dbProfileUseCase.updateCurrentPositionName(pos!![0].name)
                StateBundle(AppState.Success(null), null, null)
            } else {
                StateBundle(AppState.Success(null), R.id.PositionFragment, null)
            }
        } else {
            StateBundle(AppState.Error("400"), null, null)
        }
    }
}