package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.melo.navigation.Screen
import com.example.melo.ui.theme.*
import com.example.melo.viewmodel.AuthViewModel
import com.example.melo.viewmodel.TicketViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    ticketViewModel: TicketViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val tickets by ticketViewModel.tickets.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            ticketViewModel.loadTicketsForUser(user.id)
        }
    }

    if (currentUser == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Movies.route) {
                popUpTo(Screen.Movies.route) { inclusive = true }
            }
        }
        return
    }

    Scaffold(
        containerColor = DarkGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkGreen)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AnimatedProfileHeader(currentUser!!.name)
<<<<<<< HEAD

            AnimatedUserInfoCard(currentUser!!)

            AnimatedStatsCard(tickets.size)

            Spacer(modifier = Modifier.weight(1f))

=======
            AnimatedUserInfoCard(currentUser!!)
            AnimatedStatsCard(tickets.size)
            Spacer(modifier = Modifier.weight(1f))
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            AnimatedLogoutButton(
                onClick = { showLogoutDialog = true }
            )
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                authViewModel.logout()
                ticketViewModel.clearTickets()
                showLogoutDialog = false
                navController.navigate(Screen.Movies.route) {
                    popUpTo(Screen.Movies.route) { inclusive = true }
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun AnimatedProfileHeader(userName: String) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "profile_header_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "avatar_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "avatar_glow"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(GoldButton.copy(alpha = glowAlpha)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                modifier = Modifier.size(60.dp),
                tint = DarkText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LightBeige,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Usuario de Cine Melo",
            fontSize = 14.sp,
            color = LightBeige.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AnimatedUserInfoCard(user: com.example.melo.model.User) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "user_info_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Información Personal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            ProfileInfoRow(
                icon = Icons.Default.Person,
                label = "Nombre",
                value = user.name
            )

            ProfileInfoRow(
                icon = Icons.Default.Email,
                label = "Email",
                value = user.email
            )
        }
    }
}

@Composable
fun AnimatedStatsCard(ticketCount: Int) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "stats_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MediumGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mis Estadísticas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LightBeige
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    number = ticketCount.toString(),
                    label = "Entradas\nCompradas"
                )

                StatItem(
                    number = if (ticketCount > 0) "⭐" else "🎬",
                    label = if (ticketCount > 0) "Usuario\nActivo" else "Nuevo\nUsuario"
                )
            }
        }
    }
}

@Composable
fun StatItem(number: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = GoldButton
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = LightBeige,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MediumGreen,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = DarkText.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = DarkText,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AnimatedLogoutButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "logout_button_scale"
    )

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
            containerColor = androidx.compose.ui.graphics.Color.Red.copy(alpha = 0.8f),
            contentColor = androidx.compose.ui.graphics.Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Cerrar Sesión",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "CERRAR SESIÓN",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LightBeige,
        title = {
            Text(
                "Cerrar Sesión",
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        },
        text = {
            Text(
                "¿Estás seguro de que quieres cerrar sesión?",
                color = DarkText
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("SÍ, CERRAR SESIÓN", color = androidx.compose.ui.graphics.Color.Red, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = MediumGreen)
            }
        }
    )
}
