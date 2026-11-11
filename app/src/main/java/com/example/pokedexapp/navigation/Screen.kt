
package com.example.pokedexapp.navigation

sealed class Screen(
    val route: String,
    val title: String
) {

    data object PokemonList : Screen("pokemon_list", "Pokédex")

    data object Types : Screen("types", "Pokémon Types")

    data object Favorites : Screen("favorites", "Favorites")

    data object PokemonDetail : Screen(
        "pokemon/{id}",
        "Pokémon Details"
    ) {
        fun createRoute(id: Int): String {
            return "pokemon/$id"
        }
    }

    data object TypePokemon : Screen(
        "type/{name}",
        "Type Pokémon"
    ) {

        fun createRoute(name: String): String {
            return "type/$name"
        }
    }
    companion object {

        val baseScreens: List<Screen>
            get() = listOf(
                PokemonList,
                Types,
                Favorites
            )
    }
}
