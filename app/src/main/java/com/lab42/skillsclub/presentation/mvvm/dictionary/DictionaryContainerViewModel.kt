package com.lab42.skillsclub.presentation.mvvm.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DictionaryContainerViewModel @Inject constructor() : ViewModel() {
    private val _route = MutableLiveData<RouteBundle>()
    val route: LiveData<RouteBundle> = _route

    private val _state = MutableLiveData<AppState>()
    val state: LiveData<AppState> = _state

    init {
        _state.postValue(AppState.Loading)
    }

    fun setRoute(route: RouteBundle) {
        _route.postValue(route)
    }

    fun setState(state: AppState) {
        _state.postValue(state)
    }
}