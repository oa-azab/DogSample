package com.omarahmed.dogsample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.omarahmed.dogsample.App
import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.domain.GetAllDogsUseCase
import com.omarahmed.dogsample.model.Dog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DogsViewModel @Inject constructor(
    private val getAllDogsUseCase: GetAllDogsUseCase
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<UiState>(UiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<UiState> = _uiState


    init {
        getAll()
    }

    fun getAll(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getAllDogsUseCase.invoke(forceRefresh)) {
                is UCResult.Success -> _uiState.value = UiState.Success(result.data)
                is UCResult.Error -> UiState.Error(result.throwable)
            }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val dogs: List<Dog>) : UiState()
        data class Error(val exception: Throwable) : UiState()
    }


    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                val getAllDogsUseCase = (application as App).appComponent.provideGetAllDogsUseCase()

                return DogsViewModel(getAllDogsUseCase) as T
            }
        }
    }

}