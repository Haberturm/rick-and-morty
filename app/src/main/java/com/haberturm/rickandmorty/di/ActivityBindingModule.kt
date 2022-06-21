package com.haberturm.rickandmorty.di

import com.haberturm.rickandmorty.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    fun provideMainActivity(): MainActivity
}