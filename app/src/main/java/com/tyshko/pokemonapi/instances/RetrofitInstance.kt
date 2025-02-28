package com.tyshko.pokemonapi.instances

import com.tyshko.pokemonapi.model.Pokemon
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService{
    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: String
    ):Response<Pokemon>
}

object RetrofitInstance {
    private const val baseUrl = "https://pokeapi.co/api/v2/"

    private fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pokeApi: PokeApiService = getInstance().create(PokeApiService::class.java)
}