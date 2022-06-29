package com.haberturm.rickandmorty.data.db.daos.locations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationResultsData

@Dao
interface LocationsDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): List<LocationResultsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: ArrayList<LocationResultsData>)

    @Query("SELECT * FROM locations WHERE id >= :lowerBound AND id <= :upperBound")
    fun getLocationsInRange(lowerBound: Int, upperBound: Int): List<LocationResultsData>

    @Query("SELECT * FROM locations WHERE " +
            "name LIKE '%' || :name || '%' AND " +
            "type LIKE '%' || :type || '%'  AND " +
            "dimension LIKE '%' || :dimension || '%'")
    fun getFilteredLocations(
        name: String,
        dimension: String,
        type: String
    ): List<LocationResultsData>
}