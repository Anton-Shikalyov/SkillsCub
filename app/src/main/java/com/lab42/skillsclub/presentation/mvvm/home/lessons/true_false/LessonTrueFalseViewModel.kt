package com.lab42.skillsclub.presentation.mvvm.home.lessons.true_false

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LessonTrueFalseViewModel : ViewModel() {
    private val _answer = MutableLiveData<String>("empty")
    val answer: LiveData<String> = _answer


    init {
        _answer.postValue("empty")
    }
    fun getAnswer(): String {
        return _answer.value.toString()
    }

    fun setAnswer(answer: String) {
        _answer.postValue(answer)
    }

}