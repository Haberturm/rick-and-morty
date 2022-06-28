package com.haberturm.rickandmorty.presentation.screens.locations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.entities.LocationUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
import com.haberturm.rickandmorty.presentation.mappers.episodes.EpisodesUiMapper
import com.haberturm.rickandmorty.presentation.mappers.locations.LocationsUiMapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class LocationsMainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<LocationUi>>>(null)
    val uiState: LiveData<UiState<List<LocationUi>>>
        get() = _uiState

    private val _nameText = MutableLiveData<String>("")
    val nameText: LiveData<String>
        get() = _nameText

    private val _dimensionText = MutableLiveData<String>("")
    val dimensionText: LiveData<String>
        get() = _dimensionText

    private val _typeText = MutableLiveData<String>("")
    val typeText: LiveData<String>
        get() = _typeText

    init {
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            _uiState.postValue(UiState.Loading)
            repository.updateLocations()
                .onEach { networkRequestState ->
                    if (networkRequestState is ApiState.Error) {
                        Log.e("EXCEPTION-network", networkRequestState.exception.toString())
                        if (networkRequestState.exception is AppException.NoInternetConnectionException) {
                            _uiState.postValue(UiState.Error(networkRequestState.exception))
                        }
                    }
                    repository.getLocations()
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Locations> -> {
                                    _uiState.postValue(
                                        UiState.Data(
                                            LocationsUiMapper().fromDomainToUi<Locations, List<LocationUi>>(
                                                data.data
                                            )
                                        )
                                    )
                                }
                                is ApiState.Error -> {
                                    Log.e("EXCEPTION-local", data.exception.toString())
                                    _uiState.postValue(UiState.Error(data.exception))
                                }
                            }
                        }.launchIn(this)
                }
                .launchIn(this)
        }
    }


    fun getFilteredData() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getFilteredLocations(
                name = nameText.value ?: "",
                type = typeText.value ?: "",
                dimension = dimensionText.value ?: ""
            ).onEach { data ->
                when (data) {
                    is ApiState.Success<Locations> -> {
                        _uiState.postValue(
                            UiState.Data(
                                LocationsUiMapper().fromDomainToUi<Locations, List<LocationUi>>(
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
    }

    fun nameTextChanger(text: CharSequence?) {
        _nameText.value = text?.toString()
    }

    fun typeTextChanger(text: CharSequence?) {
        _typeText.value = text?.toString()
    }

    fun dimensionTextChanger(text: CharSequence?) {
        _dimensionText.value = text?.toString()
    }

    fun clearFilters() {
        _nameText.value = ""
        _dimensionText.value = ""
        _typeText.value = ""
    }

    fun refreshData(){
        getData()  //в нашем случае, не обязательно перезагружать фрагмент, можно просто обновить данные
        clearFilters()
    }

    fun showDetails(){

    }
}