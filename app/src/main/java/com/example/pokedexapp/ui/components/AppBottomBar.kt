
package com.example.pokedexapp.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import com.example.pokedexapp.navigation.Screen

@Composable
fun AppBottomBar(
    selectedRoute: String,
    onNavigate: (Screen) -> Unit
) {

    NavigationBar {

        Screen.baseScreens.forEach { screen ->

            NavigationBarItem(
                selected = selectedRoute == screen.route,

                onClick = {
                    onNavigate(screen)
                },

                icon = {
                    Text(
                        when (screen) {
                            Screen.PokemonList -> "☰"
                            Screen.Types -> "◈"
                            Screen.Favorites -> "♥"
                            else -> ""
                        }
                    )
                },

                label = {
                    Text(screen.title)
                }
            )
        }
    }
}
