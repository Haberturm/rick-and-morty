package com.haberturm.rickandmorty.presentation.mappers.locations

import com.haberturm.rickandmorty.domain.entities.locations.LocationResults
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.presentation.entities.LocationDetailUi
import com.haberturm.rickandmorty.presentation.entities.LocationUi
import com.haberturm.rickandmorty.presentation.mappers.UiMapper
import com.haberturm.rickandmorty.util.Util
import com.haberturm.rickandmorty.util.Util.getIdFromUrl

class LocationsUiMapper : UiMapper() {
    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDomainToUi(data: T): D {
        if (data !is Locations){
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type Locations"
            )
        }
        val locationsDomain = (data as Locations)
        return locationsDomain.results.map { location ->
            LocationUi(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension
            )
        } as D
    }

    override fun <T, D> fromDomainToUiSingle(data: T): D {
        if (data !is LocationResults) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type LocationResults"
            )
        }
        val locationDomain = data as LocationResults
        return LocationDetailUi(
            id = locationDomain.id,
            name = locationDomain.name,
            type = locationDomain.type,
            dimension = locationDomain.dimension,
            charactersIds = locationDomain.residents.map { url ->
                getIdFromUrl(url)
            }
        ) as D
    }
}