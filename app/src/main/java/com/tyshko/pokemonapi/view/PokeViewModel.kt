package com.tyshko.pokemonapi.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyshko.pokemonapi.instances.NetworkResponse
import com.tyshko.pokemonapi.instances.RetrofitInstance
import com.tyshko.pokemonapi.model.Pokemon
import kotlinx.coroutines.launch

class PokeViewModel : ViewModel() {
    private val pokeApi = RetrofitInstance.pokeApi
    private val _pokemonList = MutableLiveData<NetworkResponse<List<Pokemon>>>(NetworkResponse.Success(emptyList()))
    val pokemonList: LiveData<NetworkResponse<List<Pokemon>>> = _pokemonList

    fun loadPokemonList() {
        val limitsOfPokemons: IntArray = intArrayOf(1, 1025)
        val amountOfDisplayedPokemons = 15

        viewModelScope.launch {
            _pokemonList.value = NetworkResponse.Loading

            try {
                val randomIds = (limitsOfPokemons[0]..limitsOfPokemons[1])
                    .shuffled()
                    .take(amountOfDisplayedPokemons)

                val pokemonListResult = mutableListOf<Pokemon>()

                randomIds.forEach { id ->
                    val response = pokeApi.getPokemon(id.toString())
                    if (response.isSuccessful) {
                        response.body()?.let { pokemon ->
                            if (pokemonListResult.none { it.name == pokemon.name }) {
                                pokemonListResult.add(pokemon)
                            }
                        }
                    }
                }

                _pokemonList.value = NetworkResponse.Success(pokemonListResult)

            } catch (e: Exception) {
                _pokemonList.value = NetworkResponse.Error("Failed to load Pokémon list: \ncheck your internet connection")
            }
        }
    }

    fun sortByName() {
        when (val current = _pokemonList.value) {
            is NetworkResponse.Success -> {
                _pokemonList.value = NetworkResponse.Success(
                    current.data.sortedBy { it.name }
                )
            }
            else -> {}
        }
    }

    fun sortByMoves() {
        when (val current = _pokemonList.value) {
            is NetworkResponse.Success -> {
                _pokemonList.value = NetworkResponse.Success(
                    current.data.sortedBy {
                        minOf(it.moves[0].move.name, it.moves[1].move.name)
                    }
                )
            }
            else -> {}
        }
    }

    fun reverseSortByName() {
        when (val current = _pokemonList.value) {
            is NetworkResponse.Success -> {
                _pokemonList.value = NetworkResponse.Success(
                    current.data.sortedByDescending { it.name }
                )
            }
            else -> {}
        }
    }

    fun reverseSortByMoves() {
        when (val current = _pokemonList.value) {
            is NetworkResponse.Success -> {
                _pokemonList.value = NetworkResponse.Success(
                    current.data.sortedByDescending {
                        minOf(it.moves[0].move.name, it.moves[1].move.name)
                    }
                )
            }
            else -> {}
        }
    }
}