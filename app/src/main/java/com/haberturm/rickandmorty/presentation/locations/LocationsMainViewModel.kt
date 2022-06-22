package com.haberturm.rickandmorty.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.LocationUi
import kotlinx.coroutines.launch
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