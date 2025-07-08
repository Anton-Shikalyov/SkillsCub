package com.lab42.skillsclub.presentation.mvvm.home.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.dto.request.GetStepByIdReqDTO
import com.lab42.skillsclub.data.dto.response.StepById
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetStepByIdUseCase
import com.lab42.skillsclub.presentation.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val getStepByIdUseCase: GetStepByIdUseCase
) : ViewModel() {
    private val _lesson = MutableLiveData<StepById>()
    val lesson: LiveData<StepById> = _lesson

    init {

    }

    fun getStepById(data: GetStepByIdReqDTO) {

        viewModelScope.launch {
            val lessonData = getStepByIdUseCase.getStepById(data)
            if (lessonData is AppState.Success<*>) {
                _lesson.postValue(lessonData.data as StepById)
            }
        }
    }

    fun getRemainingTime(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val targetDate = LocalDateTime.parse(dateStr, formatter)
        val now = LocalDateTime.now()

        if (targetDate.isBefore(now)) {
            return "Время истекло"
        }

        val totalMinutes = ChronoUnit.MINUTES.between(now, targetDate)
        val totalHours = ceil(totalMinutes / 60.0).toInt()

        return if (totalHours >= 24) {
            val totalDays = ceil(totalHours / 24.0).toInt()
            "You can retry test in $totalDays days"
        } else {
            "You can retry test in $totalHours hours"
        }
    }
}