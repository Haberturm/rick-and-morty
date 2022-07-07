package com.haberturm.rickandmorty.di.modules

import com.haberturm.rickandmorty.di.ActivityScoped
import com.haberturm.rickandmorty.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    fun provideMainActivity(): MainActivity
}