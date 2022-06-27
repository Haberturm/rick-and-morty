package com.haberturm.rickandmorty.data.db.daos.locations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.locations.LocationResultsData

@Dao
interface LocationsDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): List<LocationResultsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: ArrayList<LocationResultsData>)

    @Query("SELECT * FROM locations WHERE id >= :lowerBound AND id <= :upperBound")
    fun getLocationsInRange(lowerBound: Int, upperBound: Int): List<LocationResultsData>
}