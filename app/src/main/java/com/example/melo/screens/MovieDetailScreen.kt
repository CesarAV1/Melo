package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
import com.example.melo.model.Movie
import com.example.melo.navigation.Screen
import com.example.melo.ui.theme.*
import com.example.melo.viewmodel.AuthViewModel
import com.example.melo.viewmodel.MovieViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieId: Int,
    movieViewModel: MovieViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val movies by movieViewModel.movies.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val movie = movies.find { it.id == movieId }

    var showLoginDialog by remember { mutableStateOf(false) }

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
                        movie.title,
                        color = LightBeige,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
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
            MovieDetailContent(
                movie = movie,
                onBuyTicketClick = {
                    if (isLoggedIn) {
                        navController.navigate(Screen.SeatSelection.createRoute(movieId))
                    } else {
                        showLoginDialog = true
                    }
                }
            )
        }
    }

    if (showLoginDialog) {
        LoginRequiredDialog(
            onDismiss = { showLoginDialog = false },
            onLoginClick = {
                showLoginDialog = false
                navController.navigate(Screen.Login.route)
            }
        )
    }
}

@Composable
fun MovieDetailContent(
    movie: Movie,
    onBuyTicketClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedMoviePoster(movie)

        Spacer(modifier = Modifier.height(24.dp))

        MovieInfoSection(movie)

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedBuyTicketButton(onClick = onBuyTicketClick)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AnimatedMoviePoster(movie: Movie) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "poster_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(280.dp)
                .height(380.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        AnimatedRatingBadge(
            rating = movie.rating,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 40.dp, top = 16.dp)
        )
    }
}

@Composable
fun MovieInfoSection(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = movie.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = LightBeige,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoChip(
                text = movie.duration,
                icon = "⏱️"
            )
            InfoChip(
                text = movie.genre,
                icon = "🎭"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MediumGreen)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Sinopsis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightBeige
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = movie.description,
                    fontSize = 14.sp,
                    color = LightBeige,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun InfoChip(
    text: String,
    icon: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GoldButton)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
        }
    }
}

@Composable
fun AnimatedBuyTicketButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "buy_button_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "button_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_glow"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
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
                containerColor = GoldButton.copy(alpha = glowAlpha),
                contentColor = DarkText
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp
            )
        ) {
            Text(
                "COMPRAR ENTRADA",
                fontSize = 18.sp,
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
fun LoginRequiredDialog(
    onDismiss: () -> Unit,
    onLoginClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LightBeige,
        title = {
            Text(
                "Iniciar Sesión Requerido",
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        },
        text = {
            Text(
                "Necesitas iniciar sesión con una cuenta para comprar entradas.",
                color = DarkText
            )
        },
        confirmButton = {
            TextButton(onClick = onLoginClick) {
                Text("INICIAR SESIÓN", color = GoldButton, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = MediumGreen)
            }
        }
    )
}
