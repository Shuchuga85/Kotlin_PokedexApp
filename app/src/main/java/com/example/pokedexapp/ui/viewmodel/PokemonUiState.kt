package com.example.pokedexapp.ui.viewmodel

import com.example.pokedexapp.model.Pokemon

sealed interface PokemonUiState {

    data object Loading : PokemonUiState

    data class Success(
        val pokemon: List<Pokemon>
    ) : PokemonUiState

    data object Empty : PokemonUiState

    data class Error(
        val message: String
    ) : PokemonUiState
}