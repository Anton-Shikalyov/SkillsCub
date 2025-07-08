package com.lab42.skillsclub.presentation.mvvm_start.create_pass

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
class CreatePasswViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _authState = MutableLiveData<StateBundle>()
    val authState: LiveData<StateBundle> = _authState

    fun createPass(data: LoginAndPassReqDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.postValue(authUseCase.createPassw(data))
        }
    }
}