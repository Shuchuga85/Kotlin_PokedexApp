package com.example.pokedexapp.ui.viewmodel

import androidx.lifecycle.ViewModel

import com.example.pokedexapp.data.PokemonRepository
import com.example.pokedexapp.data.remote.PokeApiClient

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TypePokemonViewModel : ViewModel() {

    private val repository = PokemonRepository(PokeApiClient())

    private val _uiState =
        MutableStateFlow<PokemonUiState>(PokemonUiState.Loading)

    val uiState: StateFlow<PokemonUiState> =
        _uiState.asStateFlow()

    fun loadPokemonByType(typeName: String) {

        _uiState.value = PokemonUiState.Loading

        repository.getPokemonByType(typeName) { result ->

            result.onSuccess { pokemonList ->

                _uiState.value = if (pokemonList.isEmpty()) {
                    PokemonUiState.Empty
                } else {
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