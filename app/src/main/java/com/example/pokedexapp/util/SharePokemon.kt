package com.example.pokedexapp.util

import android.content.Context
import android.content.Intent

import com.example.pokedexapp.model.PokemonDetail

fun sharePokemon(
    context: Context,
    pokemon: PokemonDetail
) {

    val name = pokemon.name.replaceFirstChar {
        it.uppercase()
    }

    val types = pokemon.types.joinToString(", ")

    val abilities = pokemon.abilities.joinToString(", ")

    val stats = pokemon.stats.joinToString("\n") {
        "${it.name}: ${it.value}"
    }

    val text = """
        Pokémon: $name
        Pokédex #${pokemon.id}

        Types: $types
        Abilities: $abilities

        Height: ${pokemon.height / 10.0} m
        Weight: ${pokemon.weight / 10.0} kg

        Base Stats:
        $stats

        More information:
        https://pokeapi.co/api/v2/pokemon/${pokemon.id}
    """.trimIndent()

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }

    val shareIntent = Intent.createChooser(
        sendIntent,
        "Share Pokémon"
    )

    context.startActivity(shareIntent)
}