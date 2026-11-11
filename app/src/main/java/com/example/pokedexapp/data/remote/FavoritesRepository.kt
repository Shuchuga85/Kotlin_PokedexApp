package com.example.pokedexapp.data

import android.content.Context

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import com.example.pokedexapp.model.Pokemon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define one DataStore instance for the application
private val Context.favoritesDataStore by preferencesDataStore(
    name = "favorites"
)

class FavoritesRepository(
    private val context: Context
) {

    companion object {
        private val FAVORITES_KEY =
            stringSetPreferencesKey("favorite_pokemon")
    }

    // Read favorites as an observable Flow
    val favorites: Flow<List<Pokemon>> =
        context.favoritesDataStore.data.map { preferences ->

            val saved = preferences[FAVORITES_KEY] ?: emptySet()

            saved.mapNotNull { entry ->

                // Stored format: "25|pikachu"
                val parts = entry.split("|", limit = 2)

                val id = parts.getOrNull(0)?.toIntOrNull()
                val name = parts.getOrNull(1)

                if (id == null || id <= 0 || name.isNullOrBlank()) {
                    null
                } else {
                    Pokemon(
                        name = name,
                        url = "https://pokeapi.co/api/v2/pokemon/$id/"
                    )
                }
            }.sortedBy { pokemon ->
                pokemon.url.trimEnd('/')
                    .substringAfterLast('/')
                    .toIntOrNull() ?: Int.MAX_VALUE
            }
        }

    // Add or remove a favorite
    suspend fun toggleFavorite(id: Int, name: String) {

        context.favoritesDataStore.edit { preferences ->

            val current = (
                    preferences[FAVORITES_KEY] ?: emptySet()
                    ).toMutableSet()

            // Find the existing entry by Pokémon ID
            val existing = current.find {
                it.substringBefore("|").toIntOrNull() == id
            }

            if (existing != null) {
                current.remove(existing)
            } else {
                current.add("$id|$name")
            }

            preferences[FAVORITES_KEY] = current
        }
    }
}