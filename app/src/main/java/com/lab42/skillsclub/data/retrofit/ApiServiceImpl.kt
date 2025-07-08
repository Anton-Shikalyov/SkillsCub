package com.lab42.skillsclub.data.retrofit

import HelloResDTO
import com.lab42.skillsclub.data.dto.request.AppUsageReqDTO
import com.lab42.skillsclub.data.dto.request.ChoosePositionReqDTO
import com.lab42.skillsclub.data.dto.request.GetSectionsReqDTO
import com.lab42.skillsclub.data.dto.request.GetStepByIdReqDTO
import com.lab42.skillsclub.data.dto.request.GetStepTasksReqDTO
import com.lab42.skillsclub.data.dto.request.GetStepsReqDTO
import com.lab42.skillsclub.data.dto.request.LoginAndPassReqDTO
import com.lab42.skillsclub.data.dto.request.LoginCheckReqDTO
import com.lab42.skillsclub.data.dto.request.ProgramsByPosReqDTO
import com.lab42.skillsclub.data.dto.request.RateStepReqDTO
import com.lab42.skillsclub.data.dto.request.UpdatePasswordReqDTO
import com.lab42.skillsclub.data.dto.request.UpdateStepPassReqDTO
import com.lab42.skillsclub.data.dto.response.ChoosePositionResDTO
import com.lab42.skillsclub.data.dto.response.DictionaryResDto
import com.lab42.skillsclub.data.dto.response.GetHelpResDTO
import com.lab42.skillsclub.data.dto.response.GetSectionsResDto
import com.lab42.skillsclub.data.dto.response.GetStepByIdResDto
import com.lab42.skillsclub.data.dto.response.GetStepTasksResDTO
import com.lab42.skillsclub.data.dto.response.GetStepsResDto
import com.lab42.skillsclub.data.dto.response.LogOutResDto
import com.lab42.skillsclub.data.dto.response.LoginCheckResponseDTO
import com.lab42.skillsclub.data.dto.response.PassSuccessResponseDTO
import com.lab42.skillsclub.data.dto.response.PositionResponseDTO
import com.lab42.skillsclub.data.dto.response.ProgramsByPosResDTO
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(
    retrofitClient: RetrofitClient
): ApiService {

    private var retrofit: Retrofit = retrofitClient.getClient()
    private var retrofitToken: Retrofit = retrofitClient.getClientWithToken()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)
    private val apiServiceToken: ApiService = retrofitToken.create(ApiService::class.java)


    override suspend fun getHello(): Response<HelloResDTO> {
        return apiService.getHello()
    }


    override suspend fun  getLoginCheck(login: LoginCheckReqDTO): Response<LoginCheckResponseDTO> {
        return apiService.getLoginCheck(login)
    }

    override suspend fun createPassw(data: LoginAndPassReqDTO): Response<PassSuccessResponseDTO> {
        return apiService.createPassw(data)
    }

    override suspend fun auth(data: LoginAndPassReqDTO): Response<PassSuccessResponseDTO> {
        return apiService.auth(data)
    }

    override suspend fun getPositions(): Response<PositionResponseDTO> {
        return apiServiceToken.getPositions()
    }

    override suspend fun sendSelectedPositions(data: ChoosePositionReqDTO): Response<ChoosePositionResDTO> {
        return apiServiceToken.sendSelectedPositions(data)
    }

    override suspend fun tokenAuth(): Response<PassSuccessResponseDTO> {
        return apiServiceToken.tokenAuth()
    }

    override suspend fun getProgramsByPosition(data: ProgramsByPosReqDTO): Response<ProgramsByPosResDTO> {
        return apiServiceToken.getProgramsByPosition(data)
    }

    override suspend fun logOut(): Response<LogOutResDto> {
        return apiServiceToken.logOut()
    }

    override suspend fun getSections(data: GetSectionsReqDTO): Response<GetSectionsResDto> {
        return apiServiceToken.getSections(data)
    }

    override suspend fun getSteps(data: GetStepsReqDTO): Response<GetStepsResDto> {
        return apiServiceToken.getSteps(data)
    }

    override suspend fun getDictionary(): Response<DictionaryResDto> {
        return apiServiceToken.getDictionary()
    }

    override suspend fun updatePassword(data: UpdatePasswordReqDTO): Response<ChoosePositionResDTO> {
        return apiServiceToken.updatePassword(data)
    }

    override suspend fun deactivationAccount(): Response<ChoosePositionResDTO> {
        return apiServiceToken.deactivationAccount()
    }

    override suspend fun getHelp(): Response<GetHelpResDTO> {
        return apiServiceToken.getHelp()
    }

    override suspend fun getStepBuId(data: GetStepByIdReqDTO): Response<GetStepByIdResDto> {
        return apiServiceToken.getStepBuId(data)
    }

    override suspend fun getStepTasks(data: GetStepTasksReqDTO): Response<GetStepTasksResDTO> {
        return apiServiceToken.getStepTasks(data)
    }

    override suspend fun updateStepPass(data: UpdateStepPassReqDTO): Response<LogOutResDto> {
        return apiServiceToken.updateStepPass(data)
    }

    override suspend fun rateStep(data: RateStepReqDTO): Response<LogOutResDto> {
        return apiServiceToken.rateStep(data)
    }

    override suspend fun setAppUsage(data: AppUsageReqDTO): Response<LogOutResDto> {
        return apiServiceToken.setAppUsage(data)
    }

}