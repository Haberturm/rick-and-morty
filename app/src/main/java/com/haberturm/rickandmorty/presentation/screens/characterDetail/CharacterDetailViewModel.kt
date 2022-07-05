package com.haberturm.rickandmorty.presentation.screens.characterDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.data.api.RetrofitClient
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.characters.CharacterResults
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterDetailUi
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
import com.haberturm.rickandmorty.presentation.mappers.episodes.EpisodesUiMapper
import com.haberturm.rickandmorty.presentation.screens.charcters.CharacterListAdapter
import com.haberturm.rickandmorty.util.Util
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<CharacterDetailUi>>(null)
    val uiState: LiveData<UiState<CharacterDetailUi>>
        get() = _uiState

    private val _episodesListState = MutableLiveData<UiState<List<EpisodeUi>>>(null)
    val episodesListState: LiveData<UiState<List<EpisodeUi>>>
        get() = _episodesListState


    fun getData(id: Int) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.updateSingleCharacter(id)
                .onEach { networkRequestState ->
                    if (networkRequestState is ApiState.Error) {
                        Log.e("EXCEPTION-network", networkRequestState.exception.toString())
                        if (networkRequestState.exception is AppException.NoInternetConnectionException) {
                            _uiState.postValue(UiState.Error(networkRequestState.exception))
                        }
                    }
                    repository.getSingleCharacter(id)
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<CharacterResults> -> {
                                    delay(500) // for smooth loading screen
                                    val characterData =
                                        CharactersUiMapper().fromDomainToUiSingle<CharacterResults, CharacterDetailUi>(
                                            data.data
                                        )
                                    _uiState.postValue(
                                        UiState.Data(
                                            characterData
                                        )
                                    )
                                    getEpisodes(characterData.episodesIds)
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

    private fun getEpisodes(ids: List<Int>) {
        _episodesListState.value = UiState.Loading
        viewModelScope.launch {
            repository.updateEpisodesByIdList(ids)
                .onEach {
                    repository.getEpisodesByIdList(ids)
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Episodes> -> {
                                    delay(500) // for smooth loading screen
                                    if (data.data.results.isEmpty()) {
                                        Log.e("EXCEPTION", "EMPTY_PAGE")
                                        _episodesListState.postValue(
                                            UiState.Error(
                                                AppException.UnknownException(   //todo: сделать отдельную ошибку на случай пустой бд
                                                    "EMPTY_PAGE"
                                                )
                                            )
                                        )
                                    } else {
                                        _episodesListState.postValue(
                                            UiState.Data(
                                                EpisodesUiMapper().fromDomainToUi<Episodes, List<EpisodeUi>>(
                                                    data.data
                                                )
                                            )
                                        )
                                    }
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
}