package com.example.pokedexapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import coil3.compose.AsyncImage
import com.example.pokedexapp.model.typeIconUrl
import com.example.pokedexapp.model.pokemonArtworkUrl
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.Box
import com.example.pokedexapp.ui.viewmodel.PokemonDetailUiState

@Composable
fun PokemonDetailScreen(
    uiState: PokemonDetailUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {

    when (uiState) {

        is PokemonDetailUiState.Loading -> {

            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PokemonDetailUiState.Error -> {

            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text("Error: ${uiState.message}")

                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }

        is PokemonDetailUiState.Success -> {

            val pokemon = uiState.pokemon

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = pokemon.name.replaceFirstChar {
                        it.uppercase()
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                AsyncImage(
                    model = pokemon.imageUrl
                        ?: pokemonArtworkUrl(pokemon.id),

                    contentDescription = "${pokemon.name} artwork",

                    modifier = Modifier
                        .size(230.dp)
                        .align(Alignment.CenterHorizontally),

                    contentScale = ContentScale.Fit,

                    placeholder = painterResource(
                        android.R.drawable.ic_menu_gallery
                    ),

                    error = painterResource(
                        android.R.drawable.ic_menu_report_image
                    )
                )

                Text(
                    text = "Pokédex #${pokemon.id}",
                    fontSize = 18.sp
                )

                HorizontalDivider()

                Text("Types", fontWeight = FontWeight.Bold)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    pokemon.types.forEach { typeName ->

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {

                            val iconUrl = typeIconUrl(typeName)

                            if (iconUrl != null) {

                                AsyncImage(
                                    model = iconUrl,
                                    contentDescription = "$typeName type",
                                    modifier = Modifier.size(44.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Text(
                                text = typeName.replaceFirstChar {
                                    it.uppercase()
                                }
                            )
                        }
                    }
                }

                Text("Abilities", fontWeight = FontWeight.Bold)

                Text(pokemon.abilities.joinToString(", "))

                HorizontalDivider()

                // PokéAPI height is in decimetres
                Text("Height: ${pokemon.height / 10.0} m")

                // PokéAPI weight is in hectograms
                Text("Weight: ${pokemon.weight / 10.0} kg")

                HorizontalDivider()

                Text(
                    "Base Stats",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                for (stat in pokemon.stats) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(stat.name)

                        Text(
                            text = stat.value.toString(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}