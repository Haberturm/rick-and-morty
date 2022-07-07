package com.haberturm.rickandmorty.di.modules

import com.haberturm.rickandmorty.data.repositories.RepositoryImpl
import com.haberturm.rickandmorty.domain.repositories.Repository
import dagger.Binds
import dagger.Module

@Module(includes = [DataBaseModule::class, CoroutineDispatcherModule::class])
interface DataModule {

    @Binds
    fun bindRepositoryToRepositoryImpl(
        newsRepositoryImpl: RepositoryImpl
    ): Repository

}