package com.haberturm.rickandmorty.di

import com.haberturm.rickandmorty.data.repositories.RepositoryImpl
import com.haberturm.rickandmorty.domain.repositories.Repository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {
    @Binds
    fun bindRepositoryToRepositoryImpl(
        newsRepositoryImpl: RepositoryImpl
    ): Repository
}