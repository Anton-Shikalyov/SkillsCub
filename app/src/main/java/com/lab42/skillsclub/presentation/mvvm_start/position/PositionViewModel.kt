package com.lab42.skillsclub.presentation.mvvm_start.position

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.dto.request.ChoosePositionReqDTO
import com.lab42.skillsclub.data.dto.response.PositionItemDTO
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.PositionUseCase
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.SentPositionUseCase
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val positionUseCase: PositionUseCase,
    private val sentPositionUseCase: SentPositionUseCase,
    private val dbProfileUseCase: DBProfileUseCase
): ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _positions = MutableLiveData<List<PositionItemDTO>?>()
    val positions: LiveData<List<PositionItemDTO>?> = _positions

    private val _localState = MutableLiveData<AppState>()
    val localState: LiveData<AppState> = _localState

    private val _state = MutableLiveData<StateBundle>()
    val state: LiveData<StateBundle> = _state

    private val _logOut = MutableLiveData<Int>()
    val logOut: LiveData<Int> = _logOut

    private val _pressedFlag = MutableLiveData(true)
    val pressedFlag: LiveData<Boolean> = _pressedFlag

    init {
        viewModelScope.launch {
            _userName.postValue(dbProfileUseCase.getProfile()[0].name)
            try {

                when (val positions = positionUseCase.getPositions()) {
                    is AppState.Success<*> -> {
                        _positions.postValue(positions.data as List<PositionItemDTO>)
                    }
                    is AppState.Error -> {
                        if (positions.code == "401") {
                            _logOut.postValue(1)
                        }
                    } else -> {
                    _state.postValue(StateBundle(AppState.Error("400"), null, null))
                }
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                _state.postValue(StateBundle(AppState.Error("404"), null, null))
            }
            catch (e: SocketTimeoutException) {
                Log.e("Error", "Timeout: ${e.message}")
                _state.postValue(StateBundle(AppState.Error("404"), null, null))
            }
        }
    }

    fun sendSelectedPosition(pos: Int) {
        viewModelScope.launch {
            _localState.postValue(sentPositionUseCase.sendPosition(ChoosePositionReqDTO(pos.toString())))
        }
    }

    fun setPositionDB(pos: Int) {
        viewModelScope.launch {
            dbProfileUseCase.updateCurrentPosition(pos)
        }
    }


    fun setPositionNameDB(name: String) {
        viewModelScope.launch {
            dbProfileUseCase.updateCurrentPositionName(name)
        }
    }

    fun pressF(b: Boolean) {
        _pressedFlag.postValue(b)
    }
}