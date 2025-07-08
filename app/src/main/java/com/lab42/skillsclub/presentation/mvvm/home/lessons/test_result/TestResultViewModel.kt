package com.lab42.skillsclub.presentation.mvvm.home.lessons.test_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.data_base.db_use_case.DBHelloUseCase
import com.lab42.skillsclub.data.data_base.entity.ConfigEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TestResultViewModel @Inject constructor(
    private val dbHelloUseCase: DBHelloUseCase
) : ViewModel() {
    private val _config= MutableLiveData<ConfigEntity?>()
    val config: LiveData<ConfigEntity?> = _config

    init {
        viewModelScope.launch {
            dbHelloUseCase.getHelloConfig()
                .collect { list ->
                    withContext(Dispatchers.Main) {
                        _config.value = list.firstOrNull()
                    }
                }
        }
    }

}