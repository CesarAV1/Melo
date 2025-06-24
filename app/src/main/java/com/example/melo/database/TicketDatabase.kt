package com.example.melo.database

import android.content.Context
import android.content.SharedPreferences
import com.example.melo.model.Ticket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TicketDatabase(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("cine_melo_tickets", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val TICKETS_KEY = "tickets"
        private const val OCCUPIED_SEATS_KEY = "occupied_seats"
    }

    fun saveTicket(ticket: Ticket) {
        val tickets = getAllTickets().toMutableList()
        tickets.add(ticket)
        val ticketsJson = gson.toJson(tickets)
        sharedPreferences.edit().putString(TICKETS_KEY, ticketsJson).apply()
    }

    fun getAllTickets(): List<Ticket> {
        val ticketsJson = sharedPreferences.getString(TICKETS_KEY, null)
        return if (ticketsJson != null) {
            val type = object : TypeToken<List<Ticket>>() {}.type
            gson.fromJson(ticketsJson, type)
        } else {
            emptyList()
        }
    }

    fun getTicketsByUserId(userId: String): List<Ticket> {
        return getAllTickets().filter { it.userId == userId }
    }

    fun getTicketById(ticketId: String): Ticket? {
        return getAllTickets().find { it.id == ticketId }
    }

    fun saveOccupiedSeats(movieId: Int, date: String, time: String, seats: List<String>) {
        val key = "$movieId-$date-$time"
        val occupiedSeats = getOccupiedSeats().toMutableMap()
        val currentSeats = occupiedSeats[key] ?: emptyList()
        occupiedSeats[key] = (currentSeats + seats).distinct()

        val occupiedSeatsJson = gson.toJson(occupiedSeats)
        sharedPreferences.edit().putString(OCCUPIED_SEATS_KEY, occupiedSeatsJson).apply()
    }

    fun getOccupiedSeats(): Map<String, List<String>> {
        val occupiedSeatsJson = sharedPreferences.getString(OCCUPIED_SEATS_KEY, null)
        return if (occupiedSeatsJson != null) {
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            gson.fromJson(occupiedSeatsJson, type)
        } else {
            emptyMap()
        }
    }

    fun getOccupiedSeatsForShow(movieId: Int, date: String, time: String): List<String> {
        val key = "$movieId-$date-$time"
        return getOccupiedSeats()[key] ?: emptyList()
    }
}
