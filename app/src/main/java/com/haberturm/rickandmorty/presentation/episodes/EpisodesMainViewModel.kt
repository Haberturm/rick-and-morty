package com.haberturm.rickandmorty.presentation.episodes

import androidx.lifecycle.ViewModel
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi

class EpisodesMainViewModel : ViewModel() {

    fun showDetails(){

    }

    val list = listOf(
        EpisodeUi(
            1,
            "Pilot",
            "S01E01",
            "December 2, 2013",
            ),
        EpisodeUi(
            2,
            "Pilot",
            "S01E01",
            "December 2, 2013",
        ),
        EpisodeUi(
            3,
            "Pilot",
            "S01E01",
            "December 2, 2013",
        ),
        EpisodeUi(
            4,
            "Pilot",
            "S01E01",
            "December 2, 2013",
        ),
        EpisodeUi(
            5,
            "Pilot",
            "S01E01",
            "December 2, 2013",
        ),
        EpisodeUi(
            6,
            "Pilot",
            "S01E01",
            "December 2, 2013",
        ),
    )
}