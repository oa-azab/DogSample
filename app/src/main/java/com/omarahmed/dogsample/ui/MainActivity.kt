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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}