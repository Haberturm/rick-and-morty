package com.haberturm.rickandmorty.presentation.mappers

abstract class UiMapper {
    abstract fun <T, D>fromDomainToUi(data: T): D
}