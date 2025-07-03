package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.melo.model.Ticket
import com.example.melo.ui.theme.*
import com.example.melo.viewmodel.TicketViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavController,
    ticketId: String,
    ticketViewModel: TicketViewModel = viewModel()
) {
    val ticket = remember(ticketId) {
        ticketViewModel.getTicketById(ticketId)
    }

    if (ticket == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Entrada no encontrada", color = LightBeige)
        }
        return
    }

    Scaffold(
        containerColor = DarkGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Entrada Digital",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkGreen),
            contentAlignment = Alignment.Center
        ) {
            AnimatedDigitalTicket(ticket)
        }
    }
}

@Composable
fun AnimatedDigitalTicket(ticket: Ticket) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "ticket_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "ticket_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ticket_glow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TicketHeader()
<<<<<<< HEAD

            PerforatedLine()

            TicketContent(ticket)

            PerforatedLine()

=======
            PerforatedLine()
            TicketContent(ticket)
            PerforatedLine()
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            BarcodeSection(ticket.ticketCode)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TicketHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    colors = listOf(GoldButton, GoldButton.copy(alpha = 0.8f))
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CINE MELO",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "ENTRADA DIGITAL",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText.copy(alpha = 0.8f),
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun TicketContent(ticket: Ticket) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Card(
                modifier = Modifier.size(100.dp),
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(12.dp))

                TicketInfoRow("FECHA", ticket.date)
                TicketInfoRow("HORA", ticket.time)
                TicketInfoRow("ASIENTOS", ticket.seats.joinToString(", "))
                TicketInfoRow("PRECIO", "$${String.format("%.2f", ticket.totalPrice)}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
<<<<<<< HEAD

=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "SALA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText.copy(alpha = 0.6f)
                )
                Text(
                    text = "02",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            Column {
                Text(
                    text = "FILA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText.copy(alpha = 0.6f)
                )
                Text(
                    text = ticket.seats.firstOrNull()?.take(1) ?: "A",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            Column {
                Text(
                    text = "COMPRA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText.copy(alpha = 0.6f)
                )
                Text(
                    text = ticket.purchaseDate.split(" ")[0],
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }
}

@Composable
fun TicketInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = DarkText.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun PerforatedLine() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawPerforatedLine(this)
    }
}

fun drawPerforatedLine(drawScope: DrawScope) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    drawScope.drawLine(
        color = Color.Gray.copy(alpha = 0.5f),
        start = Offset(0f, 0f),
        end = Offset(drawScope.size.width, 0f),
        pathEffect = pathEffect,
        strokeWidth = 2f
    )
}

@Composable
fun BarcodeSection(ticketCode: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BarcodeDisplay()

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = ticketCode,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Presenta este código en taquilla",
            fontSize = 10.sp,
            color = DarkText.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BarcodeDisplay() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 40.dp)
    ) {
        val barWidth = size.width / 50
        for (i in 0 until 50) {
            val isThick = (i % 3 == 0) || (i % 7 == 0)
            val barHeight = if (isThick) size.height * 0.8f else size.height * 0.6f
            val x = i * barWidth

            drawLine(
                color = Color.Black,
                start = Offset(x, (size.height - barHeight) / 2),
                end = Offset(x, (size.height + barHeight) / 2),
                strokeWidth = barWidth * 0.8f
            )
        }
    }
}
