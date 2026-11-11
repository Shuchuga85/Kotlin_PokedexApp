# Kotlin Pokédex App

An Android Pokédex built with **Kotlin** and **Jetpack Compose** for a mobile development class project. It uses the public [PokéAPI](https://pokeapi.co/) to browse Pokémon and their details.

## Features

- Browse a list of Pokémon with artwork, names, and Pokédex numbers.
- Open a Pokémon to see its types, abilities, height, weight, and base stats.
- Explore Pokémon by type.
- Save and remove favorites, with local persistence across app restarts.
- Share Pokémon details using the Android Sharesheet.
- Navigate between Pokédex, Types, and Favorites tabs.
- Loading, empty, and error states, including retry actions for API requests.

## Tech stack

- **Kotlin** and **Jetpack Compose** (Material 3)
- **OkHttp** for asynchronous REST GET requests
- **JSONObject** for JSON parsing
- **ViewModel + StateFlow** for UI state
- **Navigation Compose** for list-to-detail navigation and bottom navigation
- **Coil 3** for asynchronous artwork loading
- **Preferences DataStore** for saved favorites

## Project structure

```text
app/src/main/java/com/example/pokedexapp/
├── data/          # API client, repositories, local favorites storage
├── model/         # Pokémon data models and artwork/type helpers
├── navigation/    # Routes and navigation graph
├── ui/
│   ├── components/  # Reusable UI elements
│   ├── screens/     # Pokémon list, details, types, favorites
│   ├── viewmodel/   # StateFlow-based screen state
│   └── theme/       # Compose theme
├── util/          # Sharing helper
├── MainActivity.kt
└── PokedexApp.kt
```

## Run the app

1. Clone this repository and open it in **Android Studio**.
2. Allow Gradle to sync and install the required dependencies.
3. Run the `app` configuration on an Android device or emulator (minimum **Android 7.0 / API 24**).
4. Ensure the device has an internet connection to retrieve Pokémon data and artwork.

No API key is required.

## Credits

Data: [PokéAPI](https://pokeapi.co/) · Artwork: [PokéAPI Sprites](https://github.com/PokeAPI/sprites).

Created as an educational Android development project.
