package com.example.melo.model

data class Ticket(
    val id: String,
    val userId: String,
    val movieId: Int,
    val movieTitle: String,
    val movieImageUrl: String,
    val date: String,
    val time: String,
    val seats: List<String>,
    val totalPrice: Double,
    val purchaseDate: String,
    val ticketCode: String
)

data class ShowTime(
    val date: String,
    val times: List<String>
)

data class SeatSelection(
    val row: String,
    val number: Int,
    val isOccupied: Boolean,
    val isSelected: Boolean = false
)
