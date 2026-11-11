
package com.example.pokedexapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.pokedexapp.ui.screens.FavoritesScreen
import com.example.pokedexapp.ui.screens.PokemonDetailScreen
import com.example.pokedexapp.ui.screens.PokemonListScreen
import com.example.pokedexapp.ui.screens.TypesScreen
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.ui.viewmodel.PokemonDetailViewModel
import com.example.pokedexapp.ui.viewmodel.PokemonViewModel
import com.example.pokedexapp.ui.viewmodel.TypePokemonViewModel
import com.example.pokedexapp.ui.viewmodel.TypesViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    favorites: List<Pokemon>,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier
    ) {

        // 1. POKÉDEX
        composable(Screen.PokemonList.route) {

            val viewModel: PokemonViewModel = viewModel()

            val uiState by viewModel.uiState
                .collectAsStateWithLifecycle()

            PokemonListScreen(
                uiState = uiState,

                onRetry = {
                    viewModel.loadPokemon()
                },

                onPokemonClick = { pokemon ->

                    val id = pokemon.url
                        .trimEnd('/')
                        .substringAfterLast('/')
                        .toIntOrNull()

                    if (id != null) {
                        navController.navigate(
                            Screen.PokemonDetail.createRoute(id)
                        ){
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // 2. TYPES
        composable(Screen.Types.route) {

            val viewModel: TypesViewModel = viewModel()

            val uiState by viewModel.uiState
                .collectAsStateWithLifecycle()

            TypesScreen(
                uiState = uiState,

                onRetry = {
                    viewModel.loadTypes()
                },

                onTypeClick = { type ->

                    navController.navigate(
                        Screen.TypePokemon.createRoute(type.name)
                    )
                }
            )
        }

        // 3. FAVORITES
        composable(Screen.Favorites.route) {

            FavoritesScreen(
                favorites = favorites,

                onPokemonClick = { pokemon ->

                    val id = pokemon.url
                        .trimEnd('/')
                        .substringAfterLast('/')
                        .toIntOrNull()

                    if (id != null) {
                        navController.navigate(
                            Screen.PokemonDetail.createRoute(id)
                        ) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable(
            route = Screen.TypePokemon.route,

            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val typeName = backStackEntry.arguments
                ?.getString("name") ?: return@composable

            val viewModel: TypePokemonViewModel = viewModel()

            val uiState by viewModel.uiState
                .collectAsStateWithLifecycle()

            LaunchedEffect(typeName) {
                viewModel.loadPokemonByType(typeName)
            }

            PokemonListScreen(
                uiState = uiState,

                onRetry = {
                    viewModel.loadPokemonByType(typeName)
                },

                onPokemonClick = { pokemon ->

                    val id = pokemon.url
                        .trimEnd('/')
                        .substringAfterLast('/')
                        .toIntOrNull()

                    if (id != null) {
                        navController.navigate(
                            Screen.PokemonDetail.createRoute(id)
                        ) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // 4. POKÉMON DETAILS
        composable(
            route = Screen.PokemonDetail.route,

            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getInt("id") ?: return@composable

            val viewModel: PokemonDetailViewModel = viewModel()

            val uiState by viewModel.uiState
                .collectAsStateWithLifecycle()

            LaunchedEffect(id) {
                viewModel.loadPokemon(id)
            }

            PokemonDetailScreen(
                uiState = uiState,

                onRetry = {
                    viewModel.loadPokemon(id)
                }
            )
        }
    }
}
