package com.haberturm.rickandmorty.di

import android.app.Application
import android.content.Context
import com.haberturm.rickandmorty.di.viewModel.ViewModelModule
import dagger.Binds
import dagger.Module

@Module(includes = [DataModule::class, ViewModelModule::class, ActivityBindingModule::class])
interface AppModule {
    @Binds
    fun bindContext(application: Application): Context
}