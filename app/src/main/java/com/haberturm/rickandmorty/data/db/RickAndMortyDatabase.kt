package com.haberturm.rickandmorty.data.db

import android.content.Context
import androidx.room.*
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData

@Database(entities = [CharacterResultsData::class], version = 1)
@TypeConverters(Converters::class)
abstract class RickAndMortyDatabase: RoomDatabase() {
    abstract fun characterDao(): CharactersDao

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