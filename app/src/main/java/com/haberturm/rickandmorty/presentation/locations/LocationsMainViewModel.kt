package com.haberturm.rickandmorty.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.LocationUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
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
                    UiState.Error(Exception(data.exception))
                }
            }
        }
    }


    fun showDetails(){

    }

    val list = listOf<LocationUi>(
        LocationUi(
            1,
            "Citadels of Ricks",
            "Space station",
            "unknown",
        ),
        LocationUi(
            2,
            "Citadels of Ricks",
            "Space station",
            "unknown",
        ),
        LocationUi(
            3,
            "Citadels of Ricks",
            "Space station",
            "unknown",
        ),
        LocationUi(
            4,
            "Citadels of Ricks",
            "Space station",
            "unknown",
        ),
        LocationUi(
            5,
            "Citadels of Ricks",
            "Space station",
            "unknown",
        ),
        LocationUi(
            6,
            "Citadels of Ricks",
            "Space station",
            "unknown",
        )
    )
}