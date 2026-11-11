package com.example.pokedexapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.example.pokedexapp.model.Pokemon

@Composable
fun FavoritesScreen(
    favorites: List<Pokemon>,
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {

    if (favorites.isEmpty()) {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No favorite Pokémon yet!")
        }

    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {

            items(
                items = favorites,
                key = { it.url }
            ) { pokemon ->

                PokemonRow(
                    pokemon = pokemon,
                    onClick = {
                        onPokemonClick(pokemon)
                    }
                )

                HorizontalDivider()
            }
        }
    }
}