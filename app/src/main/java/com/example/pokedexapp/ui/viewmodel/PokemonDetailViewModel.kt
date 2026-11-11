package com.example.pokedexapp.ui.viewmodel

import androidx.lifecycle.ViewModel

import com.example.pokedexapp.data.PokemonRepository
import com.example.pokedexapp.data.remote.PokeApiClient
import com.example.pokedexapp.model.PokemonDetail

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface PokemonDetailUiState {

    data object Loading : PokemonDetailUiState

    data class Success(
        val pokemon: PokemonDetail
    ) : PokemonDetailUiState

    data class Error(
        val message: String
    ) : PokemonDetailUiState
}

class PokemonDetailViewModel : ViewModel() {

    private val repository = PokemonRepository(PokeApiClient())

    private val _uiState =
        MutableStateFlow<PokemonDetailUiState>(
            PokemonDetailUiState.Loading
        )

    val uiState: StateFlow<PokemonDetailUiState> =
        _uiState.asStateFlow()

    fun loadPokemon(id: Int) {

        _uiState.value = PokemonDetailUiState.Loading

        repository.getPokemonDetail(id) { result ->

            result.onSuccess { pokemon ->
                _uiState.value = PokemonDetailUiState.Success(pokemon)
            }

            result.onFailure { error ->
                _uiState.value = PokemonDetailUiState.Error(
                    error.message ?: "Unknown error"
                )
            }
        }
    }
}