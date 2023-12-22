package com.omarahmed.dogsample.ui.home

import android.util.Log
import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.domain.GetAllDogsUseCase
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.yieldOrReturn
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeView,
    private val getAllDogsUseCase: GetAllDogsUseCase
) {
    private val scope = MainScope()


    fun getDogs(forceRefresh: Boolean = false, filterTerm: String = "") {
        scope.launch {
            view.hideError()
            view.showLoading()
            when (val result = getAllDogsUseCase.invoke(forceRefresh, filterTerm).yieldOrReturn()) {
                is UCResult.Success -> {
                    view.hideLoading()
                    view.showResult(result.data)

                    result.data.forEach { Log.d("HomePresenter", it.toString()) }
                }

                is UCResult.Error -> {
                    val errorMessage = result.throwable.message
                        ?: "something went wrong, please try again later."
                    view.hideLoading()
                    view.showError(errorMessage)

                    Log.w("HomePresenter", result.throwable)
                }

            }
        }
    }

    fun cancelScope() {
        Log.d("HomePresenter", "cancelScope called")
        scope.cancel()
    }
}