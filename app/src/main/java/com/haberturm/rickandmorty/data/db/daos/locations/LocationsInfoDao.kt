package com.haberturm.rickandmorty.data.db.daos.locations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesInfoData
import com.haberturm.rickandmorty.data.entities.locations.LocationsInfoData

@Dao
interface LocationsInfoDao {
    @Query("SELECT * FROM locationsInfo")
    fun getEpisodesInfo(): LocationsInfoData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInfo(info: LocationsInfoData)
}