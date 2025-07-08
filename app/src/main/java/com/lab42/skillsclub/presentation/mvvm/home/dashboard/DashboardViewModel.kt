package com.lab42.skillsclub.presentation.mvvm.home.dashboard

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.data_base.entity.ProfileDbEntity
import com.lab42.skillsclub.data.dto.request.ProgramsByPosReqDTO
import com.lab42.skillsclub.data.dto.response.ProgramPositionItemDTO
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetProgramsByPosUseCase
import com.lab42.skillsclub.presentation.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("SuspiciousIndentation")
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dbProfileUseCase: DBProfileUseCase,
    private val getProgramsByPosUseCase: GetProgramsByPosUseCase,
): ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> = _currentPosition

    private val _programs = MutableLiveData<List<ProgramPositionItemDTO>>()
    val programs: LiveData<List<ProgramPositionItemDTO>> = _programs

    private val _item = MutableLiveData<ProgramPositionItemDTO>()
    val item: LiveData<ProgramPositionItemDTO> = _item

    private val _logOut = MutableLiveData<Int>()
    val logOut: LiveData<Int> = _logOut

    init {
        Log.i("INIT", "1111111")
        viewModelScope.launch {
            val profile: List<ProfileDbEntity> = dbProfileUseCase.getProfile()

            _userName.postValue(profile[0].name)
            _currentPosition.postValue(profile[0].currentPositionName)

            getPrograms(profile[0].currentPosition)
        }
    }
    fun getPrograms(pos: Int) {
        viewModelScope.launch {
            when (val data = getProgramsByPosUseCase.getPrograms(ProgramsByPosReqDTO(pos))) {
                is AppState.Success<*> -> {
                    if (data.data is List<*>) {
                        val programs = data.data as List<ProgramPositionItemDTO>
                        _programs.postValue(programs)
                        val profile: List<ProfileDbEntity> = dbProfileUseCase.getProfile()
                        _currentPosition.postValue(profile[0].currentPositionName)
                    }
                }
                is AppState.Error -> {
                    if (data.code == "401") {
                        _logOut.postValue(1)
                    }
                }
                else -> {

                }
            }
        }
    }

     fun setItem(item: ProgramPositionItemDTO) {
         _item.postValue(item)
    }

}


