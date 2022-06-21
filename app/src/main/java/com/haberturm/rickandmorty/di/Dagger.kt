package com.haberturm.rickandmorty.di

import com.haberturm.rickandmorty.data.repositories.RepositoryImpl
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.MainActivity
import dagger.Binds
import dagger.Component
import dagger.Module


@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}

@Module(includes = [AppBindModule::class])
class AppModule

@Module
interface AppBindModule {
    @Binds
    fun bindRepositoryToRepositoryImpl(
        newsRepositoryImpl: RepositoryImpl
    ): Repository
}