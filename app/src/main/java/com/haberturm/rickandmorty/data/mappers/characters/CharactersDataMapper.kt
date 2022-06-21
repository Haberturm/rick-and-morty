package com.haberturm.rickandmorty.data.mappers.characters

import com.haberturm.rickandmorty.data.entities.characters.CharacterResponseData
import com.haberturm.rickandmorty.data.mappers.DataMapper
import com.haberturm.rickandmorty.domain.entities.characters.*
import java.lang.IllegalArgumentException

class CharactersDataMapper() : DataMapper() {
    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDataToDomain(data: T): D {
        if (data !is CharacterResponseData) {
            throw IllegalArgumentException("Exception in ${CharactersDataMapper::class.qualifiedName}: 'data must be type CharacterResponseData'")
        }
        val charactersData = (data as CharacterResponseData)
        return Characters(
            results = charactersData.results.map { resultsData ->
                Results(
                    id = resultsData.id,
                    name = resultsData.name,
                    status = resultsData.status,
                    species = resultsData.species,
                    type = resultsData.species,
                    gender = resultsData.gender,
                    origin = Origin(
                        name = resultsData.origin.name,
                        url = resultsData.origin.url
                    ),
                    location = Location(
                        name = resultsData.location.name,
                        url = resultsData.location.url
                    ),
                    image = resultsData.image,
                    episode = resultsData.episode,
                    url = resultsData.url,
                    created = resultsData.created
                )
            } as ArrayList<Results>,
            info = Info(
                count = charactersData.info.count,
                pages = charactersData.info.pages,
                next = charactersData.info.next,
                prev = charactersData.info.prev
            )
        ) as D
    }
}