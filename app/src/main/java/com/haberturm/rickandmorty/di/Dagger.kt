package com.haberturm.rickandmorty.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.RickAndMortyApp
import com.haberturm.rickandmorty.data.repositories.RepositoryImpl
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.MainActivity
import com.haberturm.rickandmorty.presentation.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.charcters.CharactersMainViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap


@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class, AppModule::class])
interface AppComponent : AndroidInjector<RickAndMortyApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}

@Module(includes = [AppBindModule::class, ViewModelModule::class, ActivityBindingModule::class])
class AppModule

@Module
interface AppBindModule {
    @Binds
    fun bindRepositoryToRepositoryImpl(
        newsRepositoryImpl: RepositoryImpl
    ): Repository
}

@Module
interface ViewModelModule {
    @Binds
     fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CharactersMainViewModel::class)
    fun characterMainViewModel(viewModel: CharactersMainViewModel): ViewModel
}

@Module
interface ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    fun provideMainActivity(): MainActivity

}

@Module
interface FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    fun charactersMainFragment(): CharactersMainFragment

}
