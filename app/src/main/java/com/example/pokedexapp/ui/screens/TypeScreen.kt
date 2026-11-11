package com.example.pokedexapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

import com.example.pokedexapp.model.typeIconUrl
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.pokedexapp.model.PokemonType
import com.example.pokedexapp.ui.viewmodel.TypesUiState

@Composable
fun TypesScreen(
    uiState: TypesUiState,
    onRetry: () -> Unit,
    onTypeClick: (PokemonType) -> Unit,
    modifier: Modifier = Modifier
) {

    when (uiState) {

        is TypesUiState.Loading -> {

            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is TypesUiState.Success -> {

            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {

                items(
                    items = uiState.types,
                    key = { it.url }
                ) { type ->

                    TypeRow(
                        type = type,
                        onClick = {
                            onTypeClick(type)
                        }
                    )

                    HorizontalDivider()
                }
            }
        }

        is TypesUiState.Empty -> {

            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No Pokémon types found!")
            }
        }

        is TypesUiState.Error -> {

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
    }
}

@Composable
fun TypeRow(
    type: PokemonType,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val iconUrl = typeIconUrl(type.name)

        if (iconUrl != null) {
            AsyncImage(
                model = iconUrl,
                contentDescription = "${type.name} type",
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = type.name.replaceFirstChar {
                it.uppercase()
            },
            fontSize = 20.sp
        )
    }
}