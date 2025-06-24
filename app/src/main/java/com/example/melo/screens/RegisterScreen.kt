package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.melo.navigation.Screen
import com.example.melo.ui.theme.*
import com.example.melo.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val name by authViewModel.name.collectAsState()
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()

    Scaffold(
        containerColor = DarkGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "REGISTRO",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkGreen),
            contentAlignment = Alignment.Center
        ) {
            AnimatedRegisterCard(
                navController = navController,
                name = name,
                email = email,
                password = password,
                onNameChange = { authViewModel.onNameChange(it) },
                onEmailChange = { authViewModel.onEmailChange(it) },
                onPasswordChange = { authViewModel.onPasswordChange(it) },
                onRegisterClick = {
                    if (authViewModel.register()) {
                        navController.navigate(Screen.Movies.route) {
                            popUpTo(Screen.Movies.route) { inclusive = true }
                        }
                    }
                },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun AnimatedRegisterCard(
    navController: NavController,
    name: String,
    email: String,
    password: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    val cardScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "register_card_entrance"
    )

    val registerError by authViewModel.registerError.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .scale(cardScale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnimatedTitle("CINE MELO")

            Text(
                text = "Únete a nuestra comunidad",
                fontSize = 16.sp,
                color = MediumGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedTextField(
                value = name,
                onValueChange = onNameChange,
                label = "Nombre completo"
            )

            AnimatedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Correo electrónico"
            )

            AnimatedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                isPassword = true
            )

            registerError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedRegisterButton(
                onClick = onRegisterClick,
                text = "REGISTRARSE"
            )
            AnimatedTextButtonRegister(
                onClick = onLoginClick,
                text = "¿Ya tienes cuenta? Inicia sesión aquí"
            )
        }
    }
}

@Composable
fun AnimatedRegisterButton(
    onClick: () -> Unit,
    text: String
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "register_button_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 8f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "register_button_elevation"
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
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GoldButton,
            contentColor = DarkText
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation.dp
        )
    ) {
        Text(
            text,
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
fun AnimatedTextButtonRegister(
    onClick: () -> Unit,
    text: String
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "text_button_register_scale"
    )

    TextButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier.scale(scale)
    ) {
        Text(
            text,
            color = MediumGreen,
            fontSize = 14.sp
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}
