package com.example.mobilecookbook

data class Recipe(
    val name: String,
    val ingredients: String,
    val instructions: String,
    var rating: Float
)