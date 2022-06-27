package com.haberturm.rickandmorty.data.db.daos.episodes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesInfoData

@Dao
interface EpisodesInfoDao {
    @Query("SELECT * FROM episodeInfo")
    fun getEpisodesInfo(): EpisodesInfoData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInfo(info: EpisodesInfoData)
}