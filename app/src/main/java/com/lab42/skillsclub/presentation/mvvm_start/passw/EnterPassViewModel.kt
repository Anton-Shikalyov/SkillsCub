package com.lab42.skillsclub.presentation.mvvm_start.passw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.dto.request.LoginAndPassReqDTO
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.AuthUseCase
import com.lab42.skillsclub.presentation.StateBundle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterPassViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _authState = MutableLiveData<StateBundle>()
    val authState: LiveData<StateBundle> = _authState

    private val _pressedFlag = MutableLiveData(true)
    val pressedFlag: LiveData<Boolean> = _pressedFlag

    fun logIn(data: LoginAndPassReqDTO) {
       viewModelScope.launch(Dispatchers.IO) {
           _authState.postValue(authUseCase.auth(data))
       }
    }

    fun pressF(b: Boolean) {
        _pressedFlag.postValue(b)
    }
}