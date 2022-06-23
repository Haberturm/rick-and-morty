package com.haberturm.rickandmorty.presentation.locations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.LocationUi
import com.haberturm.rickandmorty.presentation.mappers.locations.LocationsUiMapper
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
            when(val data = repository.getLocations()){
                is ApiState.Success<Locations> -> {
                    _uiState.postValue(
                        UiState.Data(LocationsUiMapper().fromDomainToUi<Locations, List<LocationUi>>(data.data))
                    )
                }
                is ApiState.Error -> {
                    Log.e("EXCEPTION", data.exception.toString())
                    _uiState.postValue(UiState.Error(Exception(data.exception)))
                }
            }
        }
    }

    fun showDetails(){

    }
}