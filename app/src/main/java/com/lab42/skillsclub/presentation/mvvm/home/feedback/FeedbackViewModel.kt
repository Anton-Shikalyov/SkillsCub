package com.lab42.skillsclub.presentation.mvvm.home.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.dto.request.RateStepReqDTO
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.RateStepUseCase
import com.lab42.skillsclub.presentation.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val rateStepUseCase: RateStepUseCase
): ViewModel() {

    private val _answer = MutableLiveData(-1)
    val answer: LiveData<Int> = _answer

    private val _feedback = MutableLiveData<AppState>()
    val feedback: LiveData<AppState> = _feedback


    fun sendFeedback(data: RateStepReqDTO) {
        viewModelScope.launch {
            _feedback.postValue(rateStepUseCase.rateStep(data))
        }
    }

    fun setAnswer(answer: Int) {
        _answer.postValue(answer)
    }

}