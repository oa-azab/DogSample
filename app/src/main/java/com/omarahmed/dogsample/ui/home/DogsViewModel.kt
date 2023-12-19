package com.omarahmed.dogsample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.domain.GetAllDogsUseCase
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.model.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogsViewModel @Inject constructor(
    private val getAllDogsUseCase: GetAllDogsUseCase
) : ViewModel() {

    private val _listType = MutableStateFlow(ListType.LIST)
    val listType: StateFlow<ListType> = _listType

    private val _uiState = MutableStateFlow<UiState>(UiState.Success(emptyList()))
    val uiState: StateFlow<UiState> = _uiState

    init {
        getAll()
    }

    fun getAll(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = getAllDogsUseCase.invoke(forceRefresh)) {
                is UCResult.Success -> UiState.Success(result.data)
                is UCResult.Error -> UiState.Error(result.throwable)
            }
        }
    }

    fun changeListType() {
        _listType.value = when (listType.value) {
            ListType.LIST -> ListType.GIRD
            ListType.GIRD -> ListType.LIST
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val dogs: List<Dog>) : UiState()
        data class Error(val exception: Throwable) : UiState()
    }

}