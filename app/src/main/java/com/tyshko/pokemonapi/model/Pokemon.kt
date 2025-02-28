package com.tyshko.pokemonapi.model

data class Pokemon(
    val name: String,
    val sprites: Sprites,
    val moves: List<DataMove>,
)

data class Sprites(
    val front_default: String,
)

data class DataMove(
    val move: Move
)

data class Move(
    val name: String,
)


