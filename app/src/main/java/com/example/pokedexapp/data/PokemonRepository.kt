package com.example.pokedexapp.data

import com.example.pokedexapp.data.remote.PokeApiClient
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.model.PokemonDetail
import com.example.pokedexapp.model.PokemonType

class PokemonRepository(
    private val apiClient: PokeApiClient
) {

    fun getPokemon(
        callback: (Result<List<Pokemon>>) -> Unit
    ) {
        apiClient.getPokemon(
            limit = 800,
            offset = 0,
            callback = callback
        )
    }

    fun getPokemonDetail(
        id: Int,
        callback: (Result<PokemonDetail>) -> Unit
    ) {
        apiClient.getPokemonDetail(id, callback)
    }

    fun getTypes(
        callback: (Result<List<PokemonType>>) -> Unit
    ) {
        apiClient.getTypes(callback)
    }

    fun getPokemonByType(
        typeName: String,
        callback: (Result<List<Pokemon>>) -> Unit
    ) {
        apiClient.getPokemonByType(typeName, callback)
    }
}