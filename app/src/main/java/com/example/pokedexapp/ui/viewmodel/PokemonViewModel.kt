package com.example.pokedexapp.ui.viewmodel

import androidx.lifecycle.ViewModel

import com.example.pokedexapp.data.PokemonRepository
import com.example.pokedexapp.data.remote.PokeApiClient

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PokemonViewModel : ViewModel() {

    // Repository responsible for getting Pokémon data
    private val repository = PokemonRepository(PokeApiClient())

    // Internal observable state
    private val _uiState =
        MutableStateFlow<PokemonUiState>(PokemonUiState.Loading)

    // Public read-only state
    val uiState: StateFlow<PokemonUiState> =
        _uiState.asStateFlow()

    // Called when the ViewModel is created
    init {
        loadPokemon()
    }

    fun loadPokemon() {

        // Start loading
        _uiState.value = PokemonUiState.Loading

        repository.getPokemon { result ->

            result.onSuccess { pokemonList ->

                if (pokemonList.isEmpty()) {
                    _uiState.value = PokemonUiState.Empty
                } else {
                    _uiState.value =
                        PokemonUiState.Success(pokemonList)
                }
            }

            result.onFailure { error ->

                _uiState.value = PokemonUiState.Error(
                    error.message ?: "Unknown error"
                )
            }
        }
    }
}