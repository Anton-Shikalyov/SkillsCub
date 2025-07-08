package com.lab42.skillsclub.presentation.utils

import android.util.Log
import com.lab42.skillsclub.data.data_base.db_use_case.DBDateUseCase
import com.lab42.skillsclub.data.dto.request.AppUsageReqDTO
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.AppUsageUseCase
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUsageTime @Inject constructor(
    private val dbDateUseCase: DBDateUseCase,
    private val appUsageUseCase: AppUsageUseCase
) {

    var startTime: Long = 0
    private var formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var viewModel: MainViewModel

    suspend fun initialiseValues(viewModel: MainViewModel, start: Long) {
        this.viewModel = viewModel
        startTime = start

        withContext(Dispatchers.IO) {
            val dbUsage = dbDateUseCase.getAppUsages()
            if (dbUsage.isEmpty()) {
                val today = formatter.format(Date())
                viewModel.setAppUsage(today, 0)
            }
        }
    }

    suspend fun appUsageSend() {
        val sessionTimeInSeconds = ((System.currentTimeMillis() - startTime) / 1000).toInt()
        val today = formatter.format(Date())

        withContext(Dispatchers.IO) {
            val dbTime = dbDateUseCase.getAppUsages()
            if (dbTime.isNotEmpty()) {
                val lastDate = dbTime[0].date
                val lastMins = dbTime[0].mins

                if (lastDate == today) {
                    val updatedTime = lastMins + sessionTimeInSeconds
                    viewModel.updateTimeAppUsage(updatedTime)
                    Log.i("AppUsage", "Обновлено: $today = $updatedTime сек")
                } else {
                    if (lastMins >= 60) {
                        viewModel.sendUsageToServer(lastDate, lastMins / 60)
                    }
                    viewModel.setAppUsage(today, sessionTimeInSeconds)
                    Log.i("AppUsage", "Новый день. Запись создана: $today = $sessionTimeInSeconds сек")
                }
            }
        }
    }

    suspend fun appUsageLogOut(): AppState {
        return withContext(Dispatchers.IO) {
            val dbTime = dbDateUseCase.getAppUsages()
            if (dbTime.isNotEmpty()) {
                val sessionTimeInSeconds = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                Log.i("AppUsagedb",  dbTime[0].mins.toString())
                Log.i("AppUsagedbcurrentTimeMillis", System.currentTimeMillis().toString())
                Log.i("AppUsagedbstartTime", startTime.toString())
                Log.i("AppUsagedbstartTime", ((System.currentTimeMillis() - startTime) / 1000).toString())
                Log.i("AppUsage123", sessionTimeInSeconds.toString())
                Log.i("AppUsage123", (((System.currentTimeMillis() - startTime) / 1000) +  dbTime[0].mins).toString())
                return@withContext appUsageUseCase.updateAppUsage(AppUsageReqDTO(dbTime[0].date, (sessionTimeInSeconds + dbTime[0].mins)/60))
            } else {
                AppState.Error("401")
            }
        }
    }

    fun err() {
        viewModel.emitError(1)
    }
}
