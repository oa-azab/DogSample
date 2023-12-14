package com.omarahmed.dogsample.ui.home

import android.util.Log
import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.domain.GetAllDogsUseCase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeView,
    private val getAllDogsUseCase: GetAllDogsUseCase
) {
    private val scope = MainScope()

    fun getDogs() {
        scope.launch {
            view.hideError()
            view.showLoading()
            when (val result = getAllDogsUseCase.invoke()) {
                is UCResult.Success -> {
                    view.hideLoading()
                    view.showResult(result.data)

                    result.data.forEach {
                        Log.d("MainActivity", it.toString())
                    }
                }

                is UCResult.Error -> {
                    val errorMessage = result.throwable.message
                        ?: "something went wrong, please try again later."
                    view.hideLoading()
                    view.showError(errorMessage)

                    Log.w("MainActivity", result.throwable)
                }

            }
        }
    }

    fun cancelScope() {
        scope.cancel()
    }
}