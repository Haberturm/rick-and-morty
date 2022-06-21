package com.haberturm.rickandmorty.presentation.locations

import androidx.lifecycle.ViewModel
import com.haberturm.rickandmorty.presentation.entities.LocationUi

class LocationsMainViewModel : ViewModel() {

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