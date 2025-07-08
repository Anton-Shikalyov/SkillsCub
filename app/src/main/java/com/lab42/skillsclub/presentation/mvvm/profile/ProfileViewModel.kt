package com.lab42.skillsclub.presentation.mvvm.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.NameSpace.SYSTEM_THEME
import com.lab42.skillsclub.data.SharedPrefInterface
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.data_base.entity.ProfileDbEntity
import com.lab42.skillsclub.data.dto.request.UpdatePasswordReqDTO
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.DeactivationUseCase
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.LogOutUseCase
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.UpdatePassUseCase
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.utils.AppUsageTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  ProfileViewModel @Inject constructor(
    private val dbProfileUseCase: DBProfileUseCase,
    private val updatePassUseCase: UpdatePassUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val sharedPrefInterface: SharedPrefInterface,
    private val deactivationUseCase: DeactivationUseCase,
): ViewModel() {
    @Inject
    lateinit var appUsageTime: AppUsageTime

    private val _logOutState = MutableLiveData<AppState>()
    val logOutState: LiveData<AppState> = _logOutState

    private val _recreateState = MutableLiveData<AppState>()
    val recreateState: LiveData<AppState> = _recreateState

    private val _profile = MutableLiveData<ProfileDbEntity>()
    val profile: LiveData<ProfileDbEntity> = _profile

    private val _theme = MutableLiveData<String>()
    val theme: LiveData<String> = _theme

    private val _updatePassRes = MutableLiveData<AppState>()
    val updatePassRes: LiveData<AppState> = _updatePassRes

    private val _deactivationReqState = MutableLiveData<AppState>()
    val deactivationReqState: LiveData<AppState> = _deactivationReqState

    private val _route = MutableLiveData<RouteBundle>()
    val route: LiveData<RouteBundle> = _route

    private val _state = MutableLiveData<AppState>()
    val state: LiveData<AppState> = _state

    init {
        viewModelScope.launch {
            _state.postValue(AppState.Loading)
            _profile.postValue(dbProfileUseCase.getProfile()[0])
        }
        _theme.postValue(sharedPrefInterface.getTheme()?: SYSTEM_THEME)
    }

    fun setState(state: AppState) {
        _state.postValue(state)
    }

    fun setRoute(route: RouteBundle) {
        _route.postValue(route)
    }

    fun logOut() {
        viewModelScope.launch {
            val res = appUsageTime.appUsageLogOut()
            if (res is AppState.Error) {
                if (res.code == "401") {
                    appUsageTime.err()
                    _logOutState.postValue(logOutUseCase.logOut())
                }
            } else {
                _logOutState.postValue(logOutUseCase.logOut())
            }
        }
    }

    fun changePos(state: AppState) {
        viewModelScope.launch {
            _recreateState.postValue(state)
        }
    }

    fun updatePass(data: UpdatePasswordReqDTO) {
        viewModelScope.launch {
            _updatePassRes.postValue(updatePassUseCase.updatePass(data))
        }
    }

    fun deactivationAccount() {
        viewModelScope.launch {
           _deactivationReqState.postValue(deactivationUseCase.sendDeactivationReq())
        }
    }

}