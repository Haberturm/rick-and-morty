package com.haberturm.rickandmorty.di

import android.content.Context
import com.haberturm.rickandmorty.data.db.RickAndMortyDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class CoroutineDispatcherModule {

    @Provides
    fun provideIoDispatcher() : CoroutineDispatcher {
        return Dispatchers.IO
    }
}