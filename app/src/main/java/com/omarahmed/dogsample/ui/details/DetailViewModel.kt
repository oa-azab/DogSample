package com.omarahmed.dogsample.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.domain.GetAllDogsUseCase
import com.omarahmed.dogsample.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllDogsUseCase: GetAllDogsUseCase
) : ViewModel() {

    private val dogId: String? = savedStateHandle["dogId"]

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState


    init {
        getAll()
    }

    fun getAll(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            _uiState.value = when (val result = getAllDogsUseCase.invoke(forceRefresh)) {
                is UCResult.Success -> {
                    val detailDog = result.data.first { it.id == dogId }
                    DetailUiState.Success(detailDog)
                }
                is UCResult.Error -> DetailUiState.Error(result.throwable)
            }
        }
    }

    sealed class DetailUiState {
        data object Loading : DetailUiState()
        data class Success(val dog: Dog) : DetailUiState()
        data class Error(val exception: Throwable) : DetailUiState()
    }

}