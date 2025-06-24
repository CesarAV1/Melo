package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.melo.model.Ticket
import com.example.melo.navigation.Screen
import com.example.melo.ui.theme.*
import com.example.melo.viewmodel.TicketViewModel
import com.example.melo.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    ticketViewModel: TicketViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val tickets by ticketViewModel.tickets.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            ticketViewModel.loadTicketsForUser(user.id)
        }
    }

    Scaffold(
        containerColor = DarkGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Entradas",
                        color = LightBeige,
                        fontSize = 20.sp,
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
        if (tickets.isEmpty()) {
            EmptyCartContent()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(DarkGreen),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tickets) { ticket ->
                    AnimatedTicketCard(
                        ticket = ticket,
                        onClick = {
                            navController.navigate(Screen.TicketDetail.createRoute(ticket.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCartContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito vacío",
                modifier = Modifier.size(80.dp),
                tint = LightBeige.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No tienes entradas compradas",
                fontSize = 18.sp,
                color = LightBeige,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Compra entradas para tus películas favoritas",
                fontSize = 14.sp,
                color = LightBeige.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AnimatedTicketCard(
    ticket: Ticket,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = CineMeloAnimations.cardHover,
        label = "ticket_card_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 8f,
        animationSpec = CineMeloAnimations.cardHover,
        label = "ticket_card_elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(ticket.movieImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = ticket.movieTitle,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ticket.movieTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${ticket.date} • ${ticket.time}",
                    fontSize = 14.sp,
                    color = MediumGreen
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Asientos: ${ticket.seats.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = DarkText.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${String.format("%.2f", ticket.totalPrice)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldButton
                )
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = GoldButton)
            ) {
                Text(
                    text = "VER",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}
