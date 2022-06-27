package com.haberturm.rickandmorty.presentation.screens.episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
import com.haberturm.rickandmorty.presentation.mappers.episodes.EpisodesUiMapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class EpisodesMainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<EpisodeUi>>>(null)
    val uiState: LiveData<UiState<List<EpisodeUi>>>
        get() = _uiState

    fun getData() {
        viewModelScope.launch {
            _uiState.postValue(UiState.Loading)
            repository.updateEpisodes()
                .onEach { networkRequestState ->
                    if (networkRequestState is ApiState.Error) {
                        if (networkRequestState.exception is AppException.NoInternetConnectionException) {
                            _uiState.postValue(UiState.Error(networkRequestState.exception))
                        }
                    }
                    repository.getEpisodes()
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Episodes> -> {
                                    _uiState.postValue(
                                        UiState.Data(
                                            EpisodesUiMapper().fromDomainToUi<Episodes, List<EpisodeUi>>(
                                                data.data
                                            )
                                        )
                                    )
                                }
                                is ApiState.Error -> {
                                    Log.e("EXCEPTION", data.exception.toString())
                                    _uiState.postValue(UiState.Error(data.exception))
                                }
                            }
                        }.launchIn(this)
                }
                .launchIn(this)
        }
    }

    fun showDetails() {

    }
}