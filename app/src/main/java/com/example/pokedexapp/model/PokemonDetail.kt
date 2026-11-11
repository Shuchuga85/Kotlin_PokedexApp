package com.example.pokedexapp.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val abilities: List<String>,
    val stats: List<PokemonStat>,
    val imageUrl: String?
)

data class PokemonStat(
    val name: String,
    val value: Int
)