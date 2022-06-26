package com.haberturm.rickandmorty.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersMainViewModel
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesMainViewModel
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
    fun characterMainViewModel(viewModel: CharactersMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsMainViewModel::class)
    fun locationsMainViewModel(viewModel: LocationsMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesMainViewModel::class)
    fun episodesMainViewModel(viewModel: EpisodesMainViewModel): ViewModel
}