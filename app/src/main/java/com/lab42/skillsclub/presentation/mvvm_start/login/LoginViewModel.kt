package com.lab42.skillsclub.presentation.mvvm_start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.LoginCheckUseCase
import com.lab42.skillsclub.presentation.StateBundle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginCheckUseCase: LoginCheckUseCase
): ViewModel() {

    private val _loginState = MutableLiveData<StateBundle>()
    val loginState: LiveData<StateBundle> = _loginState

    fun logIn(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.postValue(loginCheckUseCase.getLoginCheck(login))
        }
    }


}