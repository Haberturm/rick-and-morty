package com.haberturm.rickandmorty.presentation.screens.locationDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.locations.LocationResults
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.LocationDetailUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
import com.haberturm.rickandmorty.presentation.mappers.locations.LocationsUiMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<LocationDetailUi>>(null)
    val uiState: LiveData<UiState<LocationDetailUi>>
        get() = _uiState

    private val _charactersListState = MutableLiveData<UiState<List<CharacterUi>>>(null)
    val charactersListState: LiveData<UiState<List<CharacterUi>>>
        get() = _charactersListState

    fun getData(id: Int){
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.updateSingleLocation(id)
                .onEach { networkRequestState ->
                    if (networkRequestState is ApiState.Error) {
                        Log.e("EXCEPTION-network", networkRequestState.exception.toString())
                        if (networkRequestState.exception is AppException.NoInternetConnectionException) {
                            _uiState.postValue(UiState.Error(networkRequestState.exception))
                        }
                    }
                    repository.getSingleLocation(id)
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<LocationResults> -> {
                                    delay(500) // for smooth loading screen
                                    val locationData =
                                        LocationsUiMapper().fromDomainToUiSingle<LocationResults, LocationDetailUi>(
                                            data.data
                                        )
                                    _uiState.postValue(
                                        UiState.Data(
                                            locationData
                                        )
                                    )
                                    getCharacters(locationData.charactersIds)
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

    private fun getCharacters(ids: List<Int>) {
        _charactersListState.value = UiState.Loading
        viewModelScope.launch {
            repository.updateCharactersByIdList(ids)
                .onEach {
                    repository.getCharactersByIdList(ids)
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Characters> -> {
                                    delay(500) // for smooth loading screen
                                    if (data.data.results.isEmpty()) {
                                        Log.e("EXCEPTION", "EMPTY_PAGE")
                                        _charactersListState.postValue(
                                            UiState.Error(
                                                AppException.UnknownException(   //todo: сделать отдельную ошибку на случай пустой бд
                                                    "EMPTY_PAGE"
                                                )
                                            )
                                        )
                                    } else {
                                        _charactersListState.postValue(
                                            UiState.Data(
                                                CharactersUiMapper().fromDomainToUi<Characters, List<CharacterUi>>(
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