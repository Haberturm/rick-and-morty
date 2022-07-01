package com.haberturm.rickandmorty.di

import com.haberturm.rickandmorty.presentation.screens.characterDetail.CharacterDetailFragment
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersFilterFragment
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesFilterFragment
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesMainFragment
import com.haberturm.rickandmorty.presentation.screens.locations.LocationsFilterFragment
import com.haberturm.rickandmorty.presentation.screens.locations.LocationsMainFragment
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

    @FragmentScoped
    @ContributesAndroidInjector
    fun charactersFilterFragment(): CharactersFilterFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun locationsFilterFragment(): LocationsFilterFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun episodesFilterFragment(): EpisodesFilterFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun charactersDetailFragment(): CharacterDetailFragment
}