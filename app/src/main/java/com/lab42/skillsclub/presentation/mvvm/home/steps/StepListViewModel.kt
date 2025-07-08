package com.lab42.skillsclub.presentation.mvvm.home.steps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.dto.request.GetStepsReqDTO
import com.lab42.skillsclub.data.dto.response.StepsSection
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetStepsUseCase
import com.lab42.skillsclub.presentation.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepListViewModel @Inject constructor(
    private val getStepsUseCase: GetStepsUseCase
) : ViewModel() {
    private val _steps = MutableLiveData<List<StepsSection>>()
    val steps: LiveData<List<StepsSection>> = _steps

    private val _item = MutableLiveData<StepsSection>()
    val item: LiveData<StepsSection> = _item

    private val _itemPos = MutableLiveData<Int>()
    val itemPos: LiveData<Int> = _itemPos

    private val _logOut = MutableLiveData<Int>()
    val logOut: LiveData<Int> = _logOut

    fun getSteps(data: GetStepsReqDTO) {
        viewModelScope.launch {
            when (val data =  getStepsUseCase.getSteps(data)) {
                is AppState.Success<*> -> {
                    if (data.data is List<*>) {
                        _steps.postValue(data.data as List<StepsSection>)
                    }
                }
                is AppState.Error -> {
                    if (data.code == "401") {
                        _logOut.postValue(1)
                    }
                }
                else -> {}
            }
        }
    }

    fun setItem(item: StepsSection, itemPos: Int) {
        _item.postValue(item)
        _itemPos.value = itemPos
    }
}