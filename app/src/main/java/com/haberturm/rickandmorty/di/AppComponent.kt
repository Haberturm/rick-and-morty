package com.haberturm.rickandmorty.di

import android.app.Application
import com.haberturm.rickandmorty.RickAndMortyApp
import com.haberturm.rickandmorty.di.viewModel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class, AppModule::class])
interface AppComponent : AndroidInjector<RickAndMortyApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}



