package com.haberturm.rickandmorty.data.mappers

abstract class DataMapper {
    abstract fun <T, D>fromDataToDomain(data: T): D
}