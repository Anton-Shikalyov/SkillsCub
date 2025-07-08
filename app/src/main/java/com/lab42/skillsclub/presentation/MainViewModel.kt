package com.lab42.skillsclub.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.data.SharedPrefInterface
import com.lab42.skillsclub.data.data_base.db_use_case.DBDateUseCase
import com.lab42.skillsclub.data.data_base.db_use_case.DBHelloUseCase
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.data_base.entity.AppUsageEntity
import com.lab42.skillsclub.data.dto.request.AppUsageReqDTO
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.AppUsageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPrefInterface: SharedPrefInterface,
    private val dbDateUseCase: DBDateUseCase,
    private val dbHelloUseCase: DBHelloUseCase,
    private val dbProfileUseCase: DBProfileUseCase,
    private val appUsageUseCase: AppUsageUseCase,
) : ViewModel() {

    private val _date = MutableLiveData<List<AppUsageEntity>>()
    val date: LiveData<List<AppUsageEntity>> = _date

    private val _logOut = MutableLiveData<Int>()
    val logOut: LiveData<Int> = _logOut

    private val _recreate = MutableLiveData<Int>()
    val recreate: LiveData<Int> = _recreate

    private val _theme = MutableLiveData<String>()
    val theme: LiveData<String> = _theme

    private val _showError = MutableLiveData<Int>()
    val showError: LiveData<Int> = _showError

    init {
        viewModelScope.launch {
        _theme.postValue(sharedPrefInterface.getTheme()?: NameSpace.SYSTEM_THEME)
        }
    }

    fun logOut() {
        viewModelScope.launch() {
            _logOut.postValue(1)
//            clearData()
        }
    }
    private fun clearData () {
        Log.i("AppUsage", "ClearData")

        viewModelScope.launch {
//            dbDateUseCase.deleteAppUsages()
            dbHelloUseCase.getHelloConfig()
            dbHelloUseCase.deleteStartNotification()
            dbProfileUseCase.deleteProfile()
        }
    }

    fun setTheme(theme: String) {
        sharedPrefInterface.setTheme(theme)
        _theme.postValue(theme)
    }

    fun setAppUsage(date: String, mins: Int) {
        viewModelScope.launch {
            dbDateUseCase.insertAppUsagesDao(AppUsageReqDTO(date, mins))
        }
    }

    fun updateTimeAppUsage(mins: Int) {
        viewModelScope.launch {
            dbDateUseCase.updateMins(mins)
        }
    }

    fun sendUsageToServer(date: String, mins: Int) {
        viewModelScope.launch {
            try {
                appUsageUseCase.updateAppUsage(AppUsageReqDTO(date, mins))
                Log.i("Server", "Успешно отправлено: $date - $mins сек")
            } catch (e: Exception) {
                Log.e("Server", "Ошибка отправки: ${e.message}")
            }
        }
    }

    suspend fun getAppUsageList(): List<AppUsageEntity> {
        return dbDateUseCase.getAppUsages()
    }

    fun emitError(err: Int) {
        _showError.postValue(err)
    }

}