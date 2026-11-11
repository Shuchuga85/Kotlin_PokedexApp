package com.example.pokedexapp.ui.viewmodel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.pokedexapp.data.FavoritesRepository
import com.example.pokedexapp.model.Pokemon

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = FavoritesRepository(
        application.applicationContext
    )

    val favorites: StateFlow<List<Pokemon>> =
        repository.favorites.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(id: Int, name: String) {

        viewModelScope.launch {
            repository.toggleFavorite(id, name)
        }
    }
}