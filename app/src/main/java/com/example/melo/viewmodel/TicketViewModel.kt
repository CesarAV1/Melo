package com.example.melo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.melo.model.Ticket
import com.example.melo.model.SeatSelection
import com.example.melo.model.ShowTime
import com.example.melo.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TicketViewModel(application: Application) : AndroidViewModel(application) {
    private val ticketRepository = TicketRepository(application.applicationContext)

    val tickets: StateFlow<List<Ticket>> = ticketRepository.tickets

    private val _selectedSeats = MutableStateFlow<List<SeatSelection>>(emptyList())
    val selectedSeats: StateFlow<List<SeatSelection>> = _selectedSeats

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate

    private val _selectedTime = MutableStateFlow("")
    val selectedTime: StateFlow<String> = _selectedTime

    private val _ticketQuantity = MutableStateFlow(1)
    val ticketQuantity: StateFlow<Int> = _ticketQuantity

    private val ticketPrice = 15.0

    fun loadTicketsForUser(userId: String) {
        viewModelScope.launch {
            ticketRepository.loadTicketsForUser(userId)
        }
    }

    fun clearTickets() {
        ticketRepository.clearTickets()
    }

    fun getShowTimes(): List<ShowTime> = ticketRepository.getShowTimes()

    fun getSeats(movieId: Int, date: String, time: String): List<List<SeatSelection>> {
        return ticketRepository.getSeats(movieId, date, time)
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
        _selectedSeats.value = emptyList()
    }

    fun selectTime(time: String) {
        _selectedTime.value = time
        _selectedSeats.value = emptyList()
    }

    fun toggleSeat(seat: SeatSelection) {
        val currentSelected = _selectedSeats.value.toMutableList()
        val existingIndex = currentSelected.indexOfFirst { it.row == seat.row && it.number == seat.number }

        if (existingIndex >= 0) {
            currentSelected.removeAt(existingIndex)
        } else if (currentSelected.size < _ticketQuantity.value) {
            currentSelected.add(seat.copy(isSelected = true))
        }

        _selectedSeats.value = currentSelected
    }

    fun setTicketQuantity(quantity: Int) {
        _ticketQuantity.value = quantity
        if (_selectedSeats.value.size > quantity) {
            _selectedSeats.value = _selectedSeats.value.take(quantity)
        }
    }

    fun getTotalPrice(): Double {
        return _selectedSeats.value.size * ticketPrice
    }

    fun purchaseTicket(
        userId: String,
        movieId: Int,
        movieTitle: String,
        movieImageUrl: String
    ) {
        if (_selectedSeats.value.isNotEmpty() && _selectedDate.value.isNotEmpty() && _selectedTime.value.isNotEmpty()) {
            val seatNames = _selectedSeats.value.map { "${it.row}${it.number}" }
            ticketRepository.purchaseTicket(
                userId = userId,
                movieId = movieId,
                movieTitle = movieTitle,
                movieImageUrl = movieImageUrl,
                date = _selectedDate.value,
                time = _selectedTime.value,
                selectedSeats = seatNames,
                totalPrice = getTotalPrice()
            )
            clearSelection()
        }
    }

    fun getTicketById(ticketId: String): Ticket? {
        return ticketRepository.getTicketById(ticketId)
    }

    private fun clearSelection() {
        _selectedSeats.value = emptyList()
        _selectedDate.value = ""
        _selectedTime.value = ""
        _ticketQuantity.value = 1
    }
}
