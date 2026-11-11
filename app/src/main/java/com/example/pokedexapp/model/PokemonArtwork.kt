package com.example.pokedexapp.model

private const val ARTWORK_BASE_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/" +
            "sprites/pokemon/other/official-artwork/"

fun pokemonArtworkUrl(id: Int): String {
    return "${ARTWORK_BASE_URL}${id}.png"
}

fun Pokemon.getId(): Int? {

    return url
        .trimEnd('/')
        .substringAfterLast('/')
        .toIntOrNull()
}

fun Pokemon.getArtworkUrl(): String? {

    val id = getId() ?: return null

    return pokemonArtworkUrl(id)
}