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
    private val _pokeResult = MutableLiveData<NetworkResponse<Pokemon>>()

    private val _pokemonList = MutableLiveData<List<Pokemon>>(emptyList())
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    fun loadPokemonList(){
        viewModelScope.launch {
            _pokemonList.value = emptyList()

            val randomIds = (1..1025).shuffled().take(15)

            randomIds.forEach { id ->
                getData(id.toString())
            }
        }
    }

    fun getData(id: String) {
        viewModelScope.launch {
            _pokeResult.value = NetworkResponse.Loading

            try {
                val response = pokeApi.getPokemon(id)
                if (response.isSuccessful) {
                    response.body()?.let { pokemon ->
                        _pokeResult.value = NetworkResponse.Success(pokemon)
                        val currentList = _pokemonList.value ?: emptyList()
                        if (currentList.none { it.name == pokemon.name }) {
                            _pokemonList.value = currentList + pokemon
                        }
                    }
                } else {
                    _pokeResult.value = NetworkResponse.Error("Failed to load Pokémon")
                }
            } catch (e: Exception) {
                _pokeResult.value = NetworkResponse.Error("Failed to load Pokémon: ${e.message}")
            }
        }
    }

    fun sortByName() {
        val currentList = _pokemonList.value ?: emptyList()
        _pokemonList.value = currentList.sortedBy { it.name }
    }

    fun sortByMoves() {
        val currentList = _pokemonList.value ?: emptyList()
        _pokemonList.value = currentList.sortedBy {
            minOf(it.moves[0].move.name, it.moves[1].move.name)
        }
    }

    fun reverseSortByName() {
        val currentList = _pokemonList.value ?: emptyList()
        _pokemonList.value = currentList.sortedByDescending { it.name }
    }

    fun reverseSortByMoves() {
        val currentList = _pokemonList.value ?: emptyList()
        _pokemonList.value = currentList.sortedByDescending {
            minOf(it.moves[0].move.name, it.moves[1].move.name)
        }
    }
}