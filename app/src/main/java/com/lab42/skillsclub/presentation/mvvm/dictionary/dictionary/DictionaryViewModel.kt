package com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab42.skillsclub.data.dto.response.Dictionary
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetDictionaryUseCase
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.mvvm.dictionary.DictList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private var getDictionaryUseCase: GetDictionaryUseCase
) : ViewModel() {

    private val _dictionary = MutableLiveData<List<Dictionary>>()
    val dictionary: LiveData<List<Dictionary>> = _dictionary

    private val _dictList = MutableLiveData<List<DictList>>()
    val dictList: LiveData<List<DictList>> = _dictList

    private val _searchList = MutableLiveData<List<DictList>>()
    val searchList: LiveData<List<DictList>> = _searchList

    private val indexMap = mutableMapOf<Char, Int>()

    private val _pickChar = MutableLiveData<Int>()
    val pickChar: LiveData<Int> = _pickChar

    private val _pickWord = MutableLiveData<DictList>()
    val pickWord: LiveData<DictList> = _pickWord

    private val _logOut = MutableLiveData<Int>()
    val logOut: LiveData<Int> = _logOut

    init {
        Log.d("FragmentLifecycle", "DictionaryViewModel created")

        getDictionary()
    }

    fun getDictionary() {
        viewModelScope.launch {
            when (val result = getDictionaryUseCase.getDictionary()) {
                is AppState.Success<*> -> {
                    if (result.data is List<*>) {
                        _dictionary.postValue(result.data as List<Dictionary>)
                        sortDictionary(result.data)
                    }
                }
                is AppState.Error -> {
                    _logOut.postValue(1)
                }
                else -> {}
            }
        }
    }

    fun findWords(sWord: CharSequence?) {
        if (sWord.isNullOrEmpty()) {
            _searchList.value = _dictList.value
            return
        }

        val searchChar = sWord.first().uppercaseChar()
        val startIndex = indexMap[searchChar] ?: return

        val filteredList = _dictList.value
            ?.drop(startIndex)
            ?.filter { it.type == 1 && it.word.startsWith(sWord, ignoreCase = true) }

        _searchList.postValue(filteredList ?: emptyList())
    }


    private fun sortDictionary(x: List<Dictionary>) {
        var letter: Char? = null
        val dictList = mutableListOf<DictList>()

        for (item in x) {
            when (letter) {
                null -> {
                    letter = item.word.first().uppercaseChar()
                    indexMap[letter] = dictList.size
                    dictList.add(DictList(letter.toString(), "",0))
                    dictList.add(DictList(item.word, item.description,1))
                }
                item.word.first().uppercaseChar() -> {
                    dictList.add(DictList(item.word, item.description, 1))
                }
                else -> {
                    letter = item.word.first().uppercaseChar()
                    indexMap[letter] = dictList.size
                    dictList.add(DictList(letter.toString(),"",0))
                    dictList.add(DictList(item.word, item.description,1))
                }
            }
        }
        _dictList.postValue(dictList)
    }

    fun setPickedLetter(char: Char) {
        if (indexMap[char] != null) {
            _pickChar.postValue(indexMap[char])
        }
    }

    fun setPickedWord(word: DictList) {
        _pickWord.postValue(word)
    }

}