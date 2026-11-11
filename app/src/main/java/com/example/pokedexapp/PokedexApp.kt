package com.example.pokedexapp

import androidx.compose.foundation.layout.padding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import android.content.Intent

import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.platform.LocalContext

import com.example.pokedexapp.util.sharePokemon
import androidx.compose.ui.Modifier

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.pokedexapp.model.getId
import com.example.pokedexapp.navigation.AppNavigation
import com.example.pokedexapp.navigation.Screen
import com.example.pokedexapp.ui.components.AppBottomBar

import com.example.pokedexapp.ui.viewmodel.FavoritesViewModel
import com.example.pokedexapp.ui.viewmodel.PokemonDetailViewModel
import com.example.pokedexapp.ui.viewmodel.PokemonDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexApp() {

    val context = LocalContext.current

    val navController = rememberNavController()

    // Shared Favorites ViewModel
    val favoritesViewModel: FavoritesViewModel = viewModel()

    val favorites by favoritesViewModel.favorites
        .collectAsStateWithLifecycle()

    // Current navigation destination
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route

    var selectedBaseRoute by rememberSaveable {
        mutableStateOf(Screen.PokemonList.route)
    }

    val currentBaseScreen = Screen.baseScreens.find {
        it.route == currentRoute
    }

    val isBaseScreen = currentBaseScreen != null

    LaunchedEffect(currentRoute) {
        if (currentBaseScreen != null) {
            selectedBaseRoute = currentBaseScreen.route
        }
    }

    val selectedRoute =
        currentBaseScreen?.route ?: selectedBaseRoute

    // Access the Detail ViewModel only on Pokémon detail screens.
    // It uses the same NavBackStackEntry as AppNavigation.
    val detailEntry = if (
        currentRoute == Screen.PokemonDetail.route
    ) {
        backStackEntry
    } else {
        null
    }

    val detailState = if (detailEntry != null) {

        val detailViewModel: PokemonDetailViewModel = viewModel(
            viewModelStoreOwner = detailEntry
        )

        val state by detailViewModel.uiState
            .collectAsStateWithLifecycle()

        state

    } else {
        null
    }

    val selectedPokemon =
        (detailState as? PokemonDetailUiState.Success)?.pokemon

    val isFavorite = selectedPokemon?.let { pokemon ->

        favorites.any {
            it.getId() == pokemon.id
        }

    } ?: false

    val screenTitle = when (currentRoute) {

        Screen.PokemonList.route ->
            Screen.PokemonList.title

        Screen.Types.route ->
            Screen.Types.title

        Screen.Favorites.route ->
            Screen.Favorites.title

        Screen.TypePokemon.route -> {

            val typeName = backStackEntry?.arguments
                ?.getString("name") ?: "Unknown"

            "${typeName.replaceFirstChar { it.uppercase() }} Pokémon"
        }

        Screen.PokemonDetail.route ->
            Screen.PokemonDetail.title

        else -> "Pokédex"
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(screenTitle)
                },

                navigationIcon = {

                    if (!isBaseScreen) {

                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },

                actions = {

                    if (currentRoute == Screen.PokemonDetail.route) {

                        // SHARE
                        IconButton(
                            enabled = selectedPokemon != null,

                            onClick = {
                                selectedPokemon?.let { pokemon ->
                                    sharePokemon(context, pokemon)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Pokémon"
                            )
                        }

                        // FAVORITES
                        IconButton(
                            enabled = selectedPokemon != null,

                            onClick = {
                                selectedPokemon?.let { pokemon ->

                                    favoritesViewModel.toggleFavorite(
                                        pokemon.id,
                                        pokemon.name
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Default.FavoriteBorder
                                },

                                contentDescription = if (isFavorite) {
                                    "Remove from favorites"
                                } else {
                                    "Add to favorites"
                                }
                            )
                        }
                    }
                }
            )
        },

        bottomBar = {

            if (isBaseScreen) {

                AppBottomBar(
                    selectedRoute = selectedRoute,

                    onNavigate = { screen ->

                        if (currentRoute != screen.route) {

                            selectedBaseRoute = screen.route

                            navController.navigate(screen.route) {

                                popUpTo(
                                    navController.graph
                                        .findStartDestination().id
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }

    ) { innerPadding ->

        AppNavigation(
            navController = navController,
            favorites = favorites,
            modifier = Modifier.padding(innerPadding)
        )
    }
}