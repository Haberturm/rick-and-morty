package com.haberturm.rickandmorty.presentation.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import javax.inject.Inject

class EpisodesMainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<EpisodeUi>>>(null)
    val uiState: LiveData<UiState<List<EpisodeUi>>>
        get() = _uiState

    fun getData(){

    }

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