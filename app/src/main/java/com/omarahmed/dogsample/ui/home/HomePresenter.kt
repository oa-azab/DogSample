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

    private val originalData: MutableList<Dog> = mutableListOf()
    private val filteredData: MutableList<Dog> = mutableListOf()

    fun getDogs(forceRefresh: Boolean = false) {
        scope.launch {
            view.hideError()
            view.showLoading()
            when (val result = getAllDogsUseCase.invoke(forceRefresh).yieldOrReturn()) {
                is UCResult.Success -> {

                    originalData.clear()
                    originalData.addAll(result.data)

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

    fun filter(filterTerm: String) {
        view.showLoading()

        filteredData.clear()
        filteredData.addAll(originalData.filter {
            it.breed.name.contains(
                filterTerm,
                ignoreCase = true
            )
        })

        view.hideLoading()
        view.showResult(filteredData)
    }

    fun cancelScope() {
        Log.d("HomePresenter", "cancelScope called")
        scope.cancel()
    }
}