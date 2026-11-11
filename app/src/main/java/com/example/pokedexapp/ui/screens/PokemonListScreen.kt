package com.example.pokedexapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import coil3.compose.AsyncImage

import com.example.pokedexapp.model.getId
import com.example.pokedexapp.model.getArtworkUrl
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.ui.viewmodel.PokemonUiState

@Composable
fun PokemonListScreen(
    uiState: PokemonUiState,
    onRetry: () -> Unit,
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {

    when (uiState) {

        is PokemonUiState.Loading -> {

            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PokemonUiState.Success -> {

            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {

                items(
                    items = uiState.pokemon,
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

        is PokemonUiState.Empty -> {

            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No Pokémon found!")
            }
        }

        is PokemonUiState.Error -> {

            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text("Error: ${uiState.message}")

                Button(
                    onClick = onRetry
                ) {
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
fun PokemonRow(
    pokemon: Pokemon,
    onClick: () -> Unit
) {

    val id = pokemon.getId()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Load official Pokémon artwork
        AsyncImage(
            model = pokemon.getArtworkUrl(),

            contentDescription = pokemon.name,

            modifier = Modifier.size(72.dp),

            contentScale = ContentScale.Fit,

            placeholder = painterResource(
                android.R.drawable.ic_menu_gallery
            ),

            error = painterResource(
                android.R.drawable.ic_menu_report_image
            )
        )

        Column {

            Text(
                text = if (id != null) {
                    "#${id.toString().padStart(3, '0')}"
                } else {
                    "#???"
                },

                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = pokemon.name.replaceFirstChar {
                    it.uppercase()
                },
                fontSize = 20.sp
            )
        }
    }
}