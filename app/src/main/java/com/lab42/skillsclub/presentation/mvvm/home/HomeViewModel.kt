package com.lab42.skillsclub.presentation.mvvm.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.data_base.entity.ProfileDbEntity
import com.lab42.skillsclub.data.dto.request.GetStepTasksReqDTO
import com.lab42.skillsclub.data.dto.request.StepTasksData
import com.lab42.skillsclub.data.dto.request.UpdateStepPassData
import com.lab42.skillsclub.data.dto.request.UpdateStepPassReqDTO
import com.lab42.skillsclub.data.dto.response.GetStepTasksData
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetStepTasksUseCase
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.UpdateStepPassUseCase
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dbProfileUseCase: DBProfileUseCase,
    private val getStepTasksUseCase: GetStepTasksUseCase,
    private val updateStepPassUseCase: UpdateStepPassUseCase
) : ViewModel() {
    private val _answer = MutableLiveData<Int?>()
    val answer: LiveData<Int?> = _answer

    private val _lessons = MutableLiveData<List<GetStepTasksData>>()
    val lessons: LiveData<List<GetStepTasksData>> = _lessons

    private val _route = MutableLiveData<RouteBundle>()
    val route: LiveData<RouteBundle> = _route

    private val _profile = MutableLiveData<ProfileDbEntity>()
    val profile: LiveData<ProfileDbEntity> = _profile

    private val _sectionsBundle = MutableLiveData<Bundle>()
    val sectionsBundle: LiveData<Bundle> = _sectionsBundle

    private val _stepsBundle = MutableLiveData<Bundle?>()
    val stepsBundle: LiveData<Bundle?> = _stepsBundle

    private val _state = MutableLiveData<AppState>()
    val state: LiveData<AppState> = _state

    // Lessons

    private val _currentLessonId = MutableLiveData<Int?>()
    val currentLessonId: LiveData<Int?> = _currentLessonId

    private val _currentSkippedLessonId = MutableLiveData<Int?>()
    val currentSkippedLessonId: LiveData<Int?> = _currentSkippedLessonId

    private val _lesson = MutableLiveData<Bundle?>()
    val lesson: LiveData<Bundle?> = _lesson

    private val _currentLessonStep = MutableLiveData<GetStepTasksData?>()
    val currentLessonStep: LiveData<GetStepTasksData?> = _currentLessonStep

    private val _lessonsScore = MutableLiveData<List<StepTasksData>>()
    val lessonsScore: LiveData<List<StepTasksData>> = _lessonsScore

    private val _lessonsScoreTotal = MutableLiveData(0)
    val lessonsScoreTotal: LiveData<Int> = _lessonsScoreTotal

    private val _openResult = MutableLiveData(0)
    val openResult: LiveData<Int> = _openResult

    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> = _currentPosition

    private val _skippedLessonsId = MutableLiveData<List<Int>>(emptyList())
    val skippedLessonsId: LiveData<List<Int>> = _skippedLessonsId

    private val _skipFlag = MutableLiveData(false)
    val skipFlag: LiveData<Boolean> = _skipFlag

    init {
        _state.postValue(AppState.Loading)
        viewModelScope.launch{
           _profile.postValue(dbProfileUseCase.getProfile()[0])
        }
    }

    fun addSkippedLessonId(id: Int) {
        val skippedLessonsId = _skippedLessonsId.value.orEmpty().toMutableList()
        skippedLessonsId.add(id)
        _skippedLessonsId.value = skippedLessonsId
    }
    fun setAnswer(answer: Int?) {
        _answer.postValue(answer)
    }

    fun setState(state: AppState) {
        Log.i("xxxxxxx", state.toString())
        _state.postValue(state)
    }

    fun setCurrentPosition(pos: String, name: String) {
        _currentPosition.postValue(pos)
        viewModelScope.launch {
            dbProfileUseCase.updateCurrentPosition(pos.toInt())
            dbProfileUseCase.updateCurrentPositionName(name)
            _profile.postValue(dbProfileUseCase.getProfile()[0])

        }
    }

    fun getLessons(data: GetStepTasksReqDTO) {
        viewModelScope.launch {
            val lessonsData = getStepTasksUseCase.getStepTasks(data)
            if (lessonsData is AppState.Success<*>) {
                _lessons.value = lessonsData.data as List<GetStepTasksData>
                _currentLessonId.postValue(0)
            }
        }
    }

    fun updateStepPass() {
        val currentBundle = _lesson.value
        val stepId = currentBundle?.getInt(NameSpace.STEP)
        viewModelScope.launch {
            val gson = Gson()
            val jsonData = if ((_lessons.value?.size ?: 0) > 0) {
                gson.toJson(
                UpdateStepPassData(
                    stepId!!,
                    (_lessonsScoreTotal.value!!.toFloat() / _lessons.value?.size!!.toFloat() * 100).toInt(),
                    _lessonsScore.value?: emptyList()
                )
            )
            } else {
                gson.toJson(
                    UpdateStepPassData(
                        stepId!!,
                        100,
                         emptyList()
                    )
                )
            }
           val result = updateStepPassUseCase.updatePass(
               UpdateStepPassReqDTO(
                   stepId.toInt(),
                   jsonData
           ))
            if (result is AppState.Success<*>) {
                _openResult.postValue(1)
            } else {
                _openResult.postValue(2)
            }
        }
    }

    fun showSkipDialog() : Boolean {
        Log.i("111111111_skippedLessonsId", _skippedLessonsId.value.toString() )
        Log.i("111111111_currentSkippe", _currentSkippedLessonId.value.toString())
        Log.i("111111111_res", (_currentLessonId.value!! + 1 == _lessons.value!!.size && (_skippedLessonsId.value?.size ?: 0) > 0).toString())
        Log.i("111111111_res", (_currentLessonId.value!! + 1).toString() + " == " + _lessons.value!!.size + " && " + _skippedLessonsId.value?.size.toString() + " > 0")
        return _currentLessonId.value!! + 1 == _lessons.value!!.size && (_skippedLessonsId.value?.size ?: 0) > 0
    }



    fun addAnswerToLessonsScore(answer: StepTasksData) {
        val currentList = _lessonsScore.value.orEmpty().toMutableList()
        currentList.add(answer)
        _lessonsScore.value = currentList
    }
    fun addAnswerToLessonsScoreById(id: Int, answer: StepTasksData) {
        val currentList = _lessonsScore.value.orEmpty().toMutableList()
        currentList[id] = answer
        _lessonsScore.value = currentList

        val skipCurrentList =  _skippedLessonsId.value.orEmpty().toMutableList()
        skipCurrentList.removeAt(_currentSkippedLessonId.value!!)
        _skippedLessonsId.value = skipCurrentList

//        val currentSkip =  _currentSkippedLessonId.value
//        if (currentSkip != null && currentSkip > 1) {
//            _currentSkippedLessonId.value = (currentSkip - 1)
//        }
    }
    fun setCurrentLessonId(id: Int) {
        _currentLessonId.postValue(id)
    }
    fun setSkippCurrentLessonId(id: Int) {
        _currentSkippedLessonId.postValue(id)
    }
    fun getCurrentLessonId(): Int {
        return _currentLessonId.value?: 0
    }
    fun setCurrentLessonStep(data: GetStepTasksData) {
        _currentLessonStep.postValue(data)
    }
    fun setSkipFlag(flag: Boolean) {
        if (flag) {
            _currentLessonId.value = 0
        }
        _skipFlag.postValue(flag)
    }
    fun setRoute(route: RouteBundle) {
        _route.postValue(route)
    }
    fun setSectionsBundle(b: Bundle) {
        _sectionsBundle.postValue(b)
    }
    fun setStepsBundle(b: Bundle) {
        _stepsBundle.postValue(b)
    }

    fun setLessonBundle(b: Bundle) {
        _lesson.postValue(b)
    }

    fun getSectionsBundle(): Bundle? {
        return _sectionsBundle.value
    }

    fun getStepsBundle(): Bundle? {
        return _stepsBundle.value
    }

    fun increaseRightAnswer() {
        val lessonValue = _lessonsScoreTotal.value!!.toInt()
        _lessonsScoreTotal.postValue(lessonValue + 1)
    }

    fun clearDataSilent() {
//        _currentLessonId.value = -1

        _currentLessonId.value = null
        _lesson.value = null
        _currentLessonStep.value = null
        _lessonsScore.value = emptyList()
        _lessonsScoreTotal.value = 0
        _openResult.value = 0
        _skippedLessonsId.value = emptyList()
        _skipFlag.value = false
        _currentLessonId.value = null
    }


}