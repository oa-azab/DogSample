package com.omarahmed.dogsample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.omarahmed.dogsample.App
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.domain.GetAllDogsUseCase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val scope = MainScope()
    private lateinit var getAllDogsUseCase: GetAllDogsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllDogsUseCase = (application as App).appComponent.provideGetAllDogsUseCase()

        findViewById<Button>(R.id.btnGet).setOnClickListener { getDogs() }

    }

    private fun getDogs() {
        scope.launch {
            when (val result = getAllDogsUseCase.invoke()) {
                is UCResult.Success -> {
                    result.data.forEach {
                        Log.d("MainActivity", it.toString())
                    }
                }

                is UCResult.Error ->
                    Log.w("MainActivity", result.throwable)
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

}