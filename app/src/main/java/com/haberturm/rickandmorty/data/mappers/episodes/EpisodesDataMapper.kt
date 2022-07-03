package com.haberturm.rickandmorty.data.mappers.episodes

import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResponseData
import com.haberturm.rickandmorty.data.mappers.DataMapper
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.episodes.EpisodesInfo
import com.haberturm.rickandmorty.domain.entities.episodes.EpisodesResults
import com.haberturm.rickandmorty.util.Util

class EpisodesDataMapper : DataMapper() {
    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDataToDomain(data: T): D {
        if (data !is EpisodesResponseData) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type EpisodesResponseData"
            )
        }
        val episodesData = (data as EpisodesResponseData)
        val info = if (episodesData.info != null){
            EpisodesInfo(
                count = episodesData.info!!.count,
                pages = episodesData.info!!.pages,
                next = episodesData.info!!.next,
                prev = episodesData.info!!.prev)
        }else{
            null
        }
        return Episodes(
            info = info,
            results = episodesData.results.map{ episodesResult ->
                EpisodesResults(
                    id = episodesResult.id,
                    name = episodesResult.name,
                    airDate = episodesResult.airDate,
                    episode = episodesResult.episode,
                    characters = episodesResult.characters,
                    url = episodesResult.url,
                    created = episodesResult.created
                )
            } as ArrayList<EpisodesResults>
        ) as D
    }

    override fun <T, D> fromDataToDomainSingle(data: T): D {
        TODO("Not yet implemented")
    }
}