package com.haberturm.rickandmorty.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.presentation.charcters.CharactersMainViewModel
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
}