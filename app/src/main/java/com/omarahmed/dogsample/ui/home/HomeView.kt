package com.omarahmed.dogsample.ui.home

import com.omarahmed.dogsample.model.Dog

interface HomeView {

    fun showLoading()

    fun hideLoading()

    fun showResult(data: List<Dog>)

    fun showError(message: String)

    fun hideError()
}