package com.example.pokedexapp.data.remote

import com.example.pokedexapp.model.Pokemon

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

import com.example.pokedexapp.model.PokemonDetail
import com.example.pokedexapp.model.PokemonStat
import com.example.pokedexapp.model.PokemonType

import org.json.JSONObject
import java.io.IOException

class PokeApiClient {

    private val client = OkHttpClient()

    private val baseUrl = "https://pokeapi.co/api/v2/"

    fun getPokemon(
        limit: Int = 20,
        offset: Int = 0,
        callback: (Result<List<Pokemon>>) -> Unit
    ) {

        // 1. Build the HTTP GET request
        val request = Request.Builder()
            .url("${baseUrl}pokemon?limit=$limit&offset=$offset")
            .get()
            .build()

        // 2. Execute the request asynchronously
        client.newCall(request).enqueue(object : Callback {

            // 3. Called when the network request fails
            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(e))
            }

            // 4. Called when an HTTP response is received
            override fun onResponse(call: Call, response: Response) {

                response.use {

                    // Check HTTP status code
                    if (!response.isSuccessful) {
                        callback(
                            Result.failure(
                                IOException("HTTP Error: ${response.code}")
                            )
                        )
                        return
                    }

                    try {

                        // 5. Read response body
                        val jsonString = response.body?.string()
                            ?: throw IOException("Empty response body")

                        // 6. Convert JSON string into JSONObject
                        val jsonObject = JSONObject(jsonString)

                        // 7. Get the "results" JSON array
                        val results = jsonObject.getJSONArray("results")

                        val pokemonList = mutableListOf<Pokemon>()

                        // 8. Convert each JSON entry into a Kotlin object
                        for (i in 0 until results.length()) {

                            val item = results.getJSONObject(i)

                            val pokemon = Pokemon(
                                name = item.getString("name"),
                                url = item.getString("url")
                            )

                            pokemonList.add(pokemon)
                        }

                        // 9. Return the list to the caller
                        callback(Result.success(pokemonList))

                    } catch (e: Exception) {

                        // JSON parsing or response reading failed
                        callback(Result.failure(e))
                    }
                }
            }
        })
    }

    fun getPokemonDetail(
        id: Int,
        callback: (Result<PokemonDetail>) -> Unit
    ) {

        val request = Request.Builder()
            .url("${baseUrl}pokemon/$id")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {

                response.use {

                    if (!response.isSuccessful) {
                        callback(
                            Result.failure(
                                IOException("HTTP Error: ${response.code}")
                            )
                        )
                        return
                    }

                    try {

                        val jsonString = response.body?.string()
                            ?: throw IOException("Empty response body")

                        val json = JSONObject(jsonString)

                        // Read Pokémon types
                        val types = mutableListOf<String>()
                        val typesArray = json.getJSONArray("types")

                        for (i in 0 until typesArray.length()) {
                            val type = typesArray.getJSONObject(i)
                                .getJSONObject("type")
                                .getString("name")

                            types.add(type)
                        }

                        // Read Pokémon abilities
                        val abilities = mutableListOf<String>()
                        val abilitiesArray = json.getJSONArray("abilities")

                        for (i in 0 until abilitiesArray.length()) {
                            val ability = abilitiesArray.getJSONObject(i)
                                .getJSONObject("ability")
                                .getString("name")

                            abilities.add(ability)
                        }

                        // Read Pokémon stats
                        val stats = mutableListOf<PokemonStat>()
                        val statsArray = json.getJSONArray("stats")

                        for (i in 0 until statsArray.length()) {

                            val statJson = statsArray.getJSONObject(i)

                            val stat = PokemonStat(
                                name = statJson.getJSONObject("stat")
                                    .getString("name"),
                                value = statJson.getInt("base_stat")
                            )

                            stats.add(stat)
                        }
                        // Get official artwork URL from JSON
                        val imageUrl = json
                            .getJSONObject("sprites")
                            .getJSONObject("other")
                            .getJSONObject("official-artwork")
                            .optString("front_default")
                            .takeIf {
                                it.isNotBlank() && it != "null"
                            }

                        // Create our Kotlin object
                        val pokemon = PokemonDetail(
                            id = json.getInt("id"),
                            name = json.getString("name"),
                            height = json.getInt("height"),
                            weight = json.getInt("weight"),
                            types = types,
                            abilities = abilities,
                            stats = stats,
                            imageUrl = imageUrl
                        )

                        callback(Result.success(pokemon))

                    } catch (e: Exception) {
                        callback(Result.failure(e))
                    }
                }
            }
        })
    }
    fun getTypes(
        callback: (Result<List<PokemonType>>) -> Unit
    ) {

        val request = Request.Builder()
            .url("${baseUrl}type?limit=100")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {

                response.use {

                    if (!response.isSuccessful) {
                        callback(
                            Result.failure(
                                IOException("HTTP Error: ${response.code}")
                            )
                        )
                        return
                    }

                    try {
                        val jsonString = response.body?.string()
                            ?: throw IOException("Empty response body")

                        val json = JSONObject(jsonString)
                        val results = json.getJSONArray("results")

                        val types = mutableListOf<PokemonType>()

                        for (i in 0 until results.length()) {

                            val item = results.getJSONObject(i)

                            types.add(
                                PokemonType(
                                    name = item.getString("name"),
                                    url = item.getString("url")
                                )
                            )
                        }

                        callback(Result.success(types))

                    } catch (e: Exception) {
                        callback(Result.failure(e))
                    }
                }
            }
        })
    }
    fun getPokemonByType(
        typeName: String,
        callback: (Result<List<Pokemon>>) -> Unit
    ) {

        val request = Request.Builder()
            .url("${baseUrl}type/$typeName")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {

                response.use {

                    if (!response.isSuccessful) {
                        callback(
                            Result.failure(
                                IOException("HTTP Error: ${response.code}")
                            )
                        )
                        return
                    }

                    try {

                        val jsonString = response.body?.string()
                            ?: throw IOException("Empty response body")

                        val json = JSONObject(jsonString)

                        val pokemonArray = json.getJSONArray("pokemon")

                        val pokemonList = mutableListOf<Pokemon>()

                        for (i in 0 until pokemonArray.length()) {

                            val item = pokemonArray.getJSONObject(i)
                                .getJSONObject("pokemon")

                            pokemonList.add(
                                Pokemon(
                                    name = item.getString("name"),
                                    url = item.getString("url")
                                )
                            )
                        }

                        callback(Result.success(pokemonList))

                    } catch (e: Exception) {
                        callback(Result.failure(e))
                    }
                }
            }
        })
    }
}