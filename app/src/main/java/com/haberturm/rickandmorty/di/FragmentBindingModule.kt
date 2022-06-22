package com.haberturm.rickandmorty.di

import com.haberturm.rickandmorty.presentation.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.episodes.EpisodesMainFragment
import com.haberturm.rickandmorty.presentation.locations.LocationsMainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBindingModule {
    @FragmentScoped
    @ContributesAndroidInjector
    fun charactersMainFragment(): CharactersMainFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun locationsMainFragment(): LocationsMainFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun episodesMainFragment(): EpisodesMainFragment

}