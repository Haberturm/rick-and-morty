package com.haberturm.rickandmorty

import android.app.Application
import android.content.Context
import com.haberturm.rickandmorty.di.AppComponent
import com.haberturm.rickandmorty.di.DaggerAppComponent

class RickAndMortyApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is RickAndMortyApp -> appComponent
        else -> applicationContext.appComponent
    }