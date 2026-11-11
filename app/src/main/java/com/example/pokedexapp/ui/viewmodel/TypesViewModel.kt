package com.example.pokedexapp.ui.viewmodel

import androidx.lifecycle.ViewModel

import com.example.pokedexapp.data.PokemonRepository
import com.example.pokedexapp.data.remote.PokeApiClient
import com.example.pokedexapp.model.PokemonType

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface TypesUiState {

    data object Loading : TypesUiState

    data class Success(
        val types: List<PokemonType>
    ) : TypesUiState

    data object Empty : TypesUiState

    data class Error(
        val message: String
    ) : TypesUiState
}

class TypesViewModel : ViewModel() {

    private val repository = PokemonRepository(PokeApiClient())

    private val _uiState =
        MutableStateFlow<TypesUiState>(TypesUiState.Loading)

    val uiState: StateFlow<TypesUiState> =
        _uiState.asStateFlow()

    init {
        loadTypes()
    }

    fun loadTypes() {

        _uiState.value = TypesUiState.Loading

        repository.getTypes { result ->

            result.onSuccess { types ->

                _uiState.value = if (types.isEmpty()) {
                    TypesUiState.Empty
                } else {
                    TypesUiState.Success(types)
                }
            }

            result.onFailure { error ->

                _uiState.value = TypesUiState.Error(
                    error.message ?: "Unknown error"
                )
            }
        }
    }
}