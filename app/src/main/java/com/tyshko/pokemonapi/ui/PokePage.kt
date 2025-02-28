package com.tyshko.pokemonapi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.tyshko.pokemonapi.model.Pokemon
import com.tyshko.pokemonapi.view.PokeViewModel


@Composable
fun PokePage(viewModel: PokeViewModel) {
    val pokemonList by viewModel.pokemonList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadPokemonList()
    }

    var expanded by remember { mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .padding(top = 50.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {expanded = false},
                ) {
                    DropdownMenuItem(
                        text = { Text("Sort by name") },
                        onClick = { viewModel.sortByName() }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Sort by moves") },
                        onClick = { viewModel.sortByMoves() }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Reverse sort by name") },
                        onClick = { viewModel.reverseSortByName() }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Reverse sort by moves") },
                        onClick = { viewModel.reverseSortByMoves() }
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(pokemonList.size) { index ->
                GeneratePokemonCard(pokemonList[index])
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color.Red),
            onClick = { viewModel.loadPokemonList() }
        ) {
            Text(text = "Reload")
        }
    }
}

@Composable
fun GeneratePokemonCard(data: Pokemon) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier.size(120.dp),
            model = data.sprites.front_default,
            contentDescription = data.name
        )
        Text(text = data.name)
        data.moves.take(2).forEach { move ->
            Text(text = move.move.name)
        }
    }
}
