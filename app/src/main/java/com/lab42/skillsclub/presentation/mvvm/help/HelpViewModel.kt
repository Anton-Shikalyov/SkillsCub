package com.lab42.skillsclub.presentation.mvvm.help

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.dto.response.HelpData
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetHelpUseCase
import com.lab42.skillsclub.presentation.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val getHelpUseCase: GetHelpUseCase
) : ViewModel() {
    private val _helpData = MutableLiveData<List<HelpData>>()
    val helpData: LiveData<List<HelpData>> = _helpData

    private val _state = MutableLiveData<AppState>()
    val state: LiveData<AppState> = _state

    private val _logOut = MutableLiveData<Int>()
    val logOut: LiveData<Int> = _logOut

    init {
        _state.postValue(AppState.Loading)
        viewModelScope.launch {
            when (val result = getHelpUseCase.getHelp()) {
                is AppState.Success<*> -> {
                    _helpData.postValue(result.data as List<HelpData>)
                }
                is AppState.Error -> {
                    if (result.code == "401") {
                        _logOut.postValue(1)
                    }
                }
                else -> {}
            }

        }
    }

    fun setState(state: AppState) {
        _state.postValue(state)
    }

}