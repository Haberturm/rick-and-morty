package com.haberturm.rickandmorty.di.modules

import android.content.Context
import com.haberturm.rickandmorty.data.db.RickAndMortyDatabase
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @Provides
    fun provideDB(context: Context) : RickAndMortyDatabase{
        return RickAndMortyDatabase.getDatabase(context)
    }
}