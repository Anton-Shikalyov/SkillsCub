package com.lab42.skillsclub.presentation.mvvm.home.sections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.dto.request.GetSectionsReqDTO
import com.lab42.skillsclub.data.dto.response.DataSection
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetSectionsUseCase
import com.lab42.skillsclub.presentation.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val getSectionsUseCase: GetSectionsUseCase,
) : ViewModel() {

    private val _sections = MutableLiveData<List<DataSection>>()
    val sections: LiveData<List<DataSection>> = _sections

    private val _item = MutableLiveData<DataSection>()
    val item: LiveData<DataSection> = _item

    private val _logOut = MutableLiveData<Int>()
    val logOut: LiveData<Int> = _logOut


    fun getSections(data: GetSectionsReqDTO) {
        viewModelScope.launch {
            when (val result = getSectionsUseCase.getSections(data)) {
                is AppState.Success<*> -> {
                    if (result.data is List<*>) {
                        _sections.postValue(result.data as List<DataSection>)
                    }
                } is AppState.Error -> {
                    if (result.code == "401") {
                        _logOut.postValue(1)
                    }
                }
                else -> {

                }
            }
        }
    }

    fun setItem(item: DataSection) {
        _item.postValue(item)
    }

}


