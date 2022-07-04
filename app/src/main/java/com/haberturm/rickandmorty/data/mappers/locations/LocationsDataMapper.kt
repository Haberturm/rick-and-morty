package com.haberturm.rickandmorty.data.mappers.locations

import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationsResponseData
import com.haberturm.rickandmorty.data.mappers.DataMapper
import com.haberturm.rickandmorty.domain.entities.episodes.EpisodesInfo
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.entities.locations.LocationResults
import com.haberturm.rickandmorty.domain.entities.locations.LocationsInfo
import com.haberturm.rickandmorty.util.Util

class LocationsDataMapper : DataMapper() {
    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDataToDomain(data: T): D {
        if (data !is LocationsResponseData) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type LocationsResponseData"
            )
        }
        val locationsData = (data as LocationsResponseData)
        val info = if (locationsData.info != null) {
            LocationsInfo(
                count = locationsData.info!!.count,
                pages = locationsData.info!!.pages,
                next = locationsData.info!!.next,
                prev = locationsData.info!!.prev
            )
        } else {
            null
        }
        return Locations(
            results = locationsData.results.map { resultsData ->
                LocationResults(
                    id = resultsData.id,
                    name = resultsData.name,
                    type = resultsData.type,
                    dimension = resultsData.dimension,
                    residents = resultsData.residents,
                    url = resultsData.url,
                    created = resultsData.created
                )
            } as ArrayList<LocationResults>,
            info = info
        ) as D
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDataToDomainSingle(data: T): D {
        if (data !is LocationResultsData) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type LocationResultsData"
            )
        }
        val locationData = data as LocationResultsData
        return LocationResults(
            id = locationData.id,
            name = locationData.name,
            type = locationData.type,
            dimension = locationData.dimension,
            residents = locationData.residents,
            url = locationData.url,
            created = locationData.url
        ) as D
    }
}