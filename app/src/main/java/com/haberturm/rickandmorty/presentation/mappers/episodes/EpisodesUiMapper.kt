package com.haberturm.rickandmorty.presentation.mappers.episodes

import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.episodes.EpisodesResults
import com.haberturm.rickandmorty.domain.entities.locations.LocationResults
import com.haberturm.rickandmorty.presentation.entities.EpisodeDetailUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.mappers.UiMapper
import com.haberturm.rickandmorty.util.Util
import com.haberturm.rickandmorty.util.Util.getIdFromUrl

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

    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDomainToUiSingle(data: T): D {
        if (data !is EpisodesResults) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type EpisodesResults"
            )
        }
        val episodeDomain = data as EpisodesResults
        return EpisodeDetailUi(
            id = episodeDomain.id,
            name = episodeDomain.name,
            airDate = episodeDomain.airDate,
            episode = episodeDomain.episode,
            charactersIds = episodeDomain.characters.map { url ->
                getIdFromUrl(url)
            }
        ) as D
    }
}