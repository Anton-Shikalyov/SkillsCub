package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.data.data_base.AppDB
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.presentation.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val apiService: ApiServiceImpl,
    private val db: AppDB,
    private val dbProfileUseCase: DBProfileUseCase
) {
    suspend fun logOut(): AppState {
        return try {
            withContext(Dispatchers.IO) {
                apiService.logOut()
                db.clearAllTables()
                if (dbProfileUseCase.getProfile().isEmpty()) {
                    AppState.Success(null)
                } else {
                    AppState.Error("logOutError")
                }
            }
        } catch (e: Exception) {
            AppState.Error("logOutError")
        }
    }
}