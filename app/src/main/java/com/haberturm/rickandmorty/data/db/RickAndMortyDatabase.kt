package com.haberturm.rickandmorty.data.db

import android.content.Context
import androidx.room.*
import com.haberturm.rickandmorty.data.db.daos.characters.CharactersDao
import com.haberturm.rickandmorty.data.db.daos.characters.CharactersInfoDao
import com.haberturm.rickandmorty.data.db.daos.episodes.EpisodesDao
import com.haberturm.rickandmorty.data.db.daos.episodes.EpisodesInfoDao
import com.haberturm.rickandmorty.data.db.daos.locations.LocationsDao
import com.haberturm.rickandmorty.data.db.daos.locations.LocationsInfoDao
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.characters.CharactersInfoData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesInfoData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationsInfoData
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes

@Database(
    entities = [
        CharacterResultsData::class, CharactersInfoData::class,
        EpisodesResultsData::class, EpisodesInfoData::class,
        LocationResultsData::class, LocationsInfoData::class,
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract fun characterDao(): CharactersDao
    abstract fun characterInfoDao(): CharactersInfoDao

    abstract fun episodesDao(): EpisodesDao
    abstract fun episodesInfoDao(): EpisodesInfoDao

    abstract fun locationsDao(): LocationsDao
    abstract fun locationsInfoDao(): LocationsInfoDao

    companion object {
        @Volatile
        private var INSTANCE: RickAndMortyDatabase? = null

        fun getDatabase(context: Context): RickAndMortyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RickAndMortyDatabase::class.java,
                    "RickAndMortyDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}