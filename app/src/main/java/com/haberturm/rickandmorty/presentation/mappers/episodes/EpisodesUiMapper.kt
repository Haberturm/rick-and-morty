package com.haberturm.rickandmorty.presentation.mappers.episodes

import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.mappers.UiMapper
import com.haberturm.rickandmorty.util.Util

class EpisodesUiMapper : UiMapper() {
    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDomainToUi(data: T): D {
        if (data !is Episodes) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type Episodes"
            )
        }
        val episodesDomain = (data as Episodes)
        return episodesDomain.results.map { episode ->
            EpisodeUi(
                id = episode.id,
                name = episode.name,
                episode = episode.episode,
                airDate = episode.airDate
            )
        } as D
    }

    override fun <T, D> fromDomainToUiSingle(data: T): D {
        TODO("Not yet implemented")
    }
}