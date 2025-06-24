package com.example.melo.model

data class Movie(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val rating: Float,
    val genre: String,
    val description: String,
    val duration: String
)
