package com.tyshko.pokemonapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tyshko.pokemonapi.ui.PokePage
import com.tyshko.pokemonapi.ui.theme.PokemonAPITheme
import com.tyshko.pokemonapi.view.PokeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val pokeViewModel = ViewModelProvider(this)[PokeViewModel::class.java]

        setContent {
            PokemonAPITheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    PokePage(pokeViewModel)
                }
            }
        }
    }
}

