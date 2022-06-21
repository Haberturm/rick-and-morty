package com.haberturm.rickandmorty.di

import com.haberturm.rickandmorty.presentation.charcters.CharactersMainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBindingModule {
    @FragmentScoped
    @ContributesAndroidInjector
    fun charactersMainFragment(): CharactersMainFragment

}