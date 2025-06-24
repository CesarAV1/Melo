package com.example.melo.repository

import android.content.Context
import com.example.melo.database.TicketDatabase
import com.example.melo.model.Ticket
import com.example.melo.model.SeatSelection
import com.example.melo.model.ShowTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class TicketRepository(context: Context) {
    private val ticketDatabase = TicketDatabase(context)
    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    fun loadTicketsForUser(userId: String) {
        _tickets.value = ticketDatabase.getTicketsByUserId(userId)
    }

    fun clearTickets() {
        _tickets.value = emptyList()
    }

    fun getShowTimes(): List<ShowTime> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        val showTimes = mutableListOf<ShowTime>()

        repeat(4) { dayOffset ->
            if (dayOffset > 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            val date = dateFormat.format(calendar.time)
            val times = listOf("10:30 AM", "1:30 PM", "4:45 PM", "7:30 PM", "10:15 PM")
            showTimes.add(ShowTime(date, times))
        }

        return showTimes
    }

    fun getSeats(movieId: Int, date: String, time: String): List<List<SeatSelection>> {
        val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H")
        val occupiedSeats = ticketDatabase.getOccupiedSeatsForShow(movieId, date, time)
        val randomOccupiedSeats = getRandomOccupiedSeats()
        val allOccupiedSeats = (occupiedSeats + randomOccupiedSeats).distinct()

        return rows.map { row ->
            (1..12).map { seatNumber ->
                val seatId = "$row$seatNumber"
                SeatSelection(
                    row = row,
                    number = seatNumber,
                    isOccupied = allOccupiedSeats.contains(seatId)
                )
            }
        }
    }

    private fun getRandomOccupiedSeats(): List<String> {
        val allSeats = mutableListOf<String>()
        val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H")
        rows.forEach { row ->
            (1..12).forEach { number ->
                allSeats.add("$row$number")
            }
        }
        return allSeats.shuffled().take(15)
    }

    fun purchaseTicket(
        userId: String,
        movieId: Int,
        movieTitle: String,
        movieImageUrl: String,
        date: String,
        time: String,
        selectedSeats: List<String>,
        totalPrice: Double
    ) {
        val ticket = Ticket(
            id = generateTicketId(),
            userId = userId,
            movieId = movieId,
            movieTitle = movieTitle,
            movieImageUrl = movieImageUrl,
            date = date,
            time = time,
            seats = selectedSeats,
            totalPrice = totalPrice,
            purchaseDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
            ticketCode = generateTicketCode()
        )

        ticketDatabase.saveTicket(ticket)
        ticketDatabase.saveOccupiedSeats(movieId, date, time, selectedSeats)
        loadTicketsForUser(userId)
    }

    fun getTicketById(ticketId: String): Ticket? {
        return ticketDatabase.getTicketById(ticketId)
    }

    private fun generateTicketId(): String {
        return "ticket_${System.currentTimeMillis()}"
    }

    private fun generateTicketCode(): String {
        return "CM${System.currentTimeMillis().toString().takeLast(8)}"
    }
}
