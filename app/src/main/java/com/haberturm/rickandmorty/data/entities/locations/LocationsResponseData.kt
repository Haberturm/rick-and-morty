package com.haberturm.rickandmorty.data.entities.locations

import com.google.gson.annotations.SerializedName


data class LocationsResponseData (

    @SerializedName("info"    ) var info    : LocationsInfoData?,
    @SerializedName("results" ) var results : ArrayList<LocationResultsData>

)