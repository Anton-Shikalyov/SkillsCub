package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import android.util.Log
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.domain.use_cases.mapper.DictionaryMapInterface
import com.lab42.skillsclub.presentation.AppState
import javax.inject.Inject

class GetDictionaryUseCase @Inject constructor(
    private var apiService: ApiServiceImpl,
    private var dictionaryMapInterface: DictionaryMapInterface
) {
    suspend fun getDictionary() : AppState {
        return try {
            val response = apiService.getDictionary()
            dictionaryMapInterface.mapResponse(response)
        } catch (e: Exception) {
            AppState.Error("404")
        }
    }
}