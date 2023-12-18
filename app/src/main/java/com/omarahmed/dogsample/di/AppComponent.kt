package com.omarahmed.dogsample.di

import com.omarahmed.dogsample.domain.GetAllDogsUseCase
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class
    ]
)
@Singleton
interface AppComponent {

    fun provideGetAllDogsUseCase(): GetAllDogsUseCase

}