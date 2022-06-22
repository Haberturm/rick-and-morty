package com.haberturm.rickandmorty.data.entities.episodes

import com.google.gson.annotations.SerializedName


data class EpisodesResponseData (

    @SerializedName("info"    ) var info    : EpisodesInfoData,
    @SerializedName("results" ) var results : ArrayList<EpisodesResultsData>

)