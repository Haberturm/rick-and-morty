package com.haberturm.rickandmorty.presentation.screens.locations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.entities.LocationUi
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

    fun showDetails(){

    }
}