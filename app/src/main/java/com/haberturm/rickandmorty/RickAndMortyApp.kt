package com.haberturm.rickandmorty

import android.app.Application
import android.content.Context
import com.haberturm.rickandmorty.di.AppComponent
import com.haberturm.rickandmorty.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class RickAndMortyApp : DaggerApplication() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this).build()
        return appComponent
    }

}
