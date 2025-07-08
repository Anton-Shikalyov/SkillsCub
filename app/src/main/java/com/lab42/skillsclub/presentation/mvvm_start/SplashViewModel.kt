package com.lab42.skillsclub.presentation.mvvm_start

import HelloResDTO
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.data.SharedPrefInterface
import com.lab42.skillsclub.data.data_base.db_use_case.DBDateUseCase
import com.lab42.skillsclub.data.data_base.db_use_case.DBHelloUseCase
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.HelloUseCase
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.TokenAuthUseCase
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val helloUseCase: HelloUseCase,
    private val sharedPrefInterface: SharedPrefInterface,
    private val tokenAuthUseCase: TokenAuthUseCase,
    private val dbHelloUseCase: DBHelloUseCase,
    private val dbDateUseCase: DBDateUseCase
): ViewModel() {

    private val _route = MutableLiveData<StateBundle>()
    val route: LiveData<StateBundle> = _route

    private val _theme = MutableLiveData<String>()
    val theme: LiveData<String> = _theme

    private val _showError = MutableLiveData<Int>()
    val showError: LiveData<Int> = _showError
    init {
        _theme.postValue(sharedPrefInterface.getTheme()?: NameSpace.SYSTEM_THEME)
        checkToken()
    }


    fun checkToken() {
        _route.postValue(StateBundle(AppState.Splash, null, null))

        viewModelScope.launch {
            dbHelloUseCase.deleteHelloConfig()
            dbHelloUseCase.deleteStartNotification()

            val token = sharedPrefInterface.getToken()
            val hello = helloUseCase.getHelloUseCase()

            if (hello.state is AppState.Success<*> && token?.isNotEmpty() == true) {
                val data = hello.state.data as HelloResDTO

                dbHelloUseCase.insertHelloConfig(data)
                dbHelloUseCase.insertStartNotification(data)


                val auth = tokenAuthUseCase.tokenAuth()
                when (auth.state) {
                    is AppState.SuccessAuth -> {
                        _route.postValue(auth)
                    }
                    is AppState.Error -> {
                        when (auth.state.code) {
                            "nullPosition" -> {
                                _route.postValue(auth)
                            }
                            "400" -> {
                                _route.postValue(StateBundle(AppState.Error("400"), null, null))
                            }
                            "401" -> {
                                dbDateUseCase.deleteAppUsages()
                                _route.postValue(StateBundle(AppState.Error("400"), null, null))
                            }
                        }
                    }
                    else -> {
                        _route.postValue(hello)
                    }
                }

            } else {
                _route.postValue(hello)
            }
        }
    }


    fun setAppRoute(route: StateBundle) {
        _route.postValue(route)
    }

    fun emitError(err: Int) {
        _showError.postValue(err)
    }

}