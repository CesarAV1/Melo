package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.melo.model.Movie
import com.example.melo.model.SeatSelection
import com.example.melo.model.ShowTime
import com.example.melo.navigation.Screen
import com.example.melo.ui.theme.*
import com.example.melo.viewmodel.AuthViewModel
import com.example.melo.viewmodel.MovieViewModel
import com.example.melo.viewmodel.TicketViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelectionScreen(
    navController: NavController,
    movieId: Int,
    movieViewModel: MovieViewModel = viewModel(),
    ticketViewModel: TicketViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val movies by movieViewModel.movies.collectAsState()
    val selectedSeats by ticketViewModel.selectedSeats.collectAsState()
    val selectedDate by ticketViewModel.selectedDate.collectAsState()
    val selectedTime by ticketViewModel.selectedTime.collectAsState()
    val ticketQuantity by ticketViewModel.ticketQuantity.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val movie = movies.find { it.id == movieId }
    val showTimes = ticketViewModel.getShowTimes()
    val seats = remember(selectedDate, selectedTime) {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            ticketViewModel.getSeats(movieId, selectedDate, selectedTime)
        } else {
            emptyList()
        }
    }

    var showPurchaseDialog by remember { mutableStateOf(false) }

    if (movie == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Película no encontrada", color = LightBeige)
        }
        return
    }

    Scaffold(
        containerColor = DarkGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Seleccionar Asientos",
                        color = LightBeige,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    AnimatedBackButton(onClick = { navController.navigateUp() })
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(DarkGreen)
        ) {
            MovieInfoHeader(movie)

            Spacer(modifier = Modifier.height(24.dp))

            DateSelectionSection(
                showTimes = showTimes,
                selectedDate = selectedDate,
                onDateSelected = { ticketViewModel.selectDate(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))
<<<<<<< HEAD

=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            if (selectedDate.isNotEmpty()) {
                TimeSelectionSection(
                    showTimes = showTimes,
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    onTimeSelected = { ticketViewModel.selectTime(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (selectedTime.isNotEmpty()) {
                TicketQuantitySection(
                    quantity = ticketQuantity,
                    onQuantityChanged = { ticketViewModel.setTicketQuantity(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                TheaterScreen()

                Spacer(modifier = Modifier.height(16.dp))

                if (seats.isNotEmpty()) {
                    SeatSelectionGrid(
                        seats = seats,
                        selectedSeats = selectedSeats,
                        onSeatToggle = { ticketViewModel.toggleSeat(it) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SeatLegend()

                Spacer(modifier = Modifier.height(24.dp))

                if (selectedSeats.isNotEmpty()) {
                    PurchaseButton(
                        totalPrice = ticketViewModel.getTotalPrice(),
                        selectedSeatsCount = selectedSeats.size,
                        onClick = { showPurchaseDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showPurchaseDialog) {
        PurchaseConfirmationDialog(
            movie = movie,
            date = selectedDate,
            time = selectedTime,
            seats = selectedSeats,
            totalPrice = ticketViewModel.getTotalPrice(),
            onConfirm = {
                currentUser?.let { user ->
                    ticketViewModel.purchaseTicket(
                        userId = user.id,
                        movieId = movie.id,
                        movieTitle = movie.title,
                        movieImageUrl = movie.imageUrl
                    )
                    ticketViewModel.loadTicketsForUser(user.id)
                }
                showPurchaseDialog = false
                navController.navigate(Screen.Cart.route)
            },
            onDismiss = { showPurchaseDialog = false }
        )
    }
}

@Composable
fun MovieInfoHeader(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MediumGreen)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = movie.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LightBeige,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${movie.duration} • ${movie.genre}",
                fontSize = 14.sp,
                color = LightBeige.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun DateSelectionSection(
    showTimes: List<ShowTime>,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "FECHA",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = LightBeige,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(showTimes) { showTime ->
                AnimatedDateChip(
                    date = showTime.date,
                    isSelected = selectedDate == showTime.date,
                    onClick = { onDateSelected(showTime.date) }
                )
            }
        }
    }
}

@Composable
fun TimeSelectionSection(
    showTimes: List<ShowTime>,
    selectedDate: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val times = showTimes.find { it.date == selectedDate }?.times ?: emptyList()

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "HORARIO",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = LightBeige,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(times) { time ->
                AnimatedTimeChip(
                    time = time,
                    isSelected = selectedTime == time,
                    onClick = { onTimeSelected(time) }
                )
            }
        }
    }
}

@Composable
fun TicketQuantitySection(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "CANTIDAD DE ENTRADAS",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = LightBeige,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(5) { index ->
                val ticketCount = index + 1
                AnimatedQuantityChip(
                    quantity = ticketCount,
                    isSelected = quantity == ticketCount,
                    onClick = { onQuantityChanged(ticketCount) }
                )
            }
        }
    }
}

@Composable
fun TheaterScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = LightBeige)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PANTALLA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }
}

@Composable
fun SeatSelectionGrid(
    seats: List<List<SeatSelection>>,
    selectedSeats: List<SeatSelection>,
    onSeatToggle: (SeatSelection) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        seats.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { seat ->
                    val isSelected = selectedSeats.any { it.row == seat.row && it.number == seat.number }
                    AnimatedSeat(
                        seat = seat,
                        isSelected = isSelected,
                        onClick = { if (!seat.isOccupied) onSeatToggle(seat) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AnimatedSeat(
    seat: SeatSelection,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "seat_scale"
    )

    val seatColor = when {
        seat.isOccupied -> Color.Gray
        isSelected -> GoldButton
        else -> LightBeige
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .scale(scale)
            .clip(RoundedCornerShape(6.dp))
            .background(seatColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!seat.isOccupied) {
                    isPressed = true
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${seat.row}${seat.number}",
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = if (seat.isOccupied) Color.White else DarkText
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun SeatLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LegendItem(color = LightBeige, text = "Libre")
        LegendItem(color = GoldButton, text = "Seleccionado")
        LegendItem(color = Color.Gray, text = "Ocupado")
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = LightBeige
        )
    }
}

@Composable
fun AnimatedDateChip(
    date: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "date_chip_scale"
    )

    Card(
        modifier = Modifier
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GoldButton else MediumGreen
        )
    ) {
        Text(
            text = date,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) DarkText else LightBeige
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun AnimatedTimeChip(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "time_chip_scale"
    )

    Card(
        modifier = Modifier
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GoldButton else MediumGreen
        )
    ) {
        Text(
            text = time,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) DarkText else LightBeige
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun AnimatedQuantityChip(
    quantity: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "quantity_chip_scale"
    )

    Card(
        modifier = Modifier
            .size(48.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GoldButton else MediumGreen
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quantity.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) DarkText else LightBeige
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun PurchaseButton(
    totalPrice: Double,
    selectedSeatsCount: Int,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "purchase_button_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(scale),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldButton,
                contentColor = DarkText
            )
        ) {
            Text(
                "COMPRAR $selectedSeatsCount ENTRADA${if (selectedSeatsCount > 1) "S" else ""} - $${String.format("%.2f", totalPrice)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

@Composable
fun PurchaseConfirmationDialog(
    movie: Movie,
    date: String,
    time: String,
    seats: List<SeatSelection>,
    totalPrice: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LightBeige,
        title = {
            Text(
                "Confirmar Compra",
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        },
        text = {
            Column {
                Text("Película: ${movie.title}", color = DarkText)
                Text("Fecha: $date", color = DarkText)
                Text("Horario: $time", color = DarkText)
                Text("Asientos: ${seats.map { "${it.row}${it.number}" }.joinToString(", ")}", color = DarkText)
                Text("Total: $${String.format("%.2f", totalPrice)}", color = DarkText, fontWeight = FontWeight.Bold)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("CONFIRMAR", color = GoldButton, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = MediumGreen)
            }
        }
    )
}
