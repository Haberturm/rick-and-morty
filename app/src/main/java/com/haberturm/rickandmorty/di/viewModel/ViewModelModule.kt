package com.haberturm.rickandmorty.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.presentation.screens.characterDetail.CharacterDetailViewModel
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersMainViewModel
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesMainViewModel
import com.haberturm.rickandmorty.presentation.screens.locationDetail.LocationDetailViewModel
import com.haberturm.rickandmorty.presentation.screens.locations.LocationsMainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CharactersMainViewModel::class)
    fun charactersMainViewModel(viewModel: CharactersMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsMainViewModel::class)
    fun locationsMainViewModel(viewModel: LocationsMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesMainViewModel::class)
    fun episodesMainViewModel(viewModel: EpisodesMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailViewModel::class)
    fun characterDetailViewModel(viewModel: CharacterDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationDetailViewModel::class)
    fun locationDetailViewModel(viewModel: LocationDetailViewModel): ViewModel
}