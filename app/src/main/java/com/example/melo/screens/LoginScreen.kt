package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
<<<<<<< HEAD
=======
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
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
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()

    Scaffold(
        containerColor = DarkGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "INICIAR SESIÓN",
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
            AnimatedLoginCard(
                email = email,
                password = password,
                onEmailChange = { authViewModel.onEmailChange(it) },
                onPasswordChange = { authViewModel.onPasswordChange(it) },
                onLoginClick = {
                    if (authViewModel.login()) {
                        navController.navigate(Screen.Movies.route) {
                            popUpTo(Screen.Movies.route) { inclusive = true }
                        }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }
    }
}

@Composable
fun AnimatedBackButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "back_button_scale"
    )

    IconButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Volver",
            tint = LightBeige
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
fun AnimatedLoginCard(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val cardScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "card_entrance"
    )

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
                text = "Bienvenido de vuelta",
                fontSize = 16.sp,
                color = MediumGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
<<<<<<< HEAD

=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            AnimatedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Correo electrónico"
            )
<<<<<<< HEAD

=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            AnimatedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                isPassword = true
            )

            val loginError by viewModel<AuthViewModel>().loginError.collectAsState()
<<<<<<< HEAD

=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            loginError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
<<<<<<< HEAD

            Spacer(modifier = Modifier.height(8.dp))

=======
            Spacer(modifier = Modifier.height(8.dp))
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            AnimatedLoginButton(
                onClick = onLoginClick,
                text = "INICIAR SESIÓN"
            )
<<<<<<< HEAD

=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            AnimatedTextButtonLogin(
                onClick = onRegisterClick,
                text = "¿No tienes cuenta? Regístrate aquí"
            )
        }
    }
}

@Composable
fun AnimatedTitle(text: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "title_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_glow"
    )

    Text(
        text = text,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = DarkGreen.copy(alpha = glowAlpha),
        textAlign = TextAlign.Center
    )
}

@Composable
fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderWidth by animateFloatAsState(
        targetValue = if (isFocused) 2f else 1f,
        animationSpec = CineMeloAnimations.fadeInOut,
        label = "border_width"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = MediumGreen) },
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = if (isFocused) 1.02f else 1f
                scaleY = if (isFocused) 1.02f else 1f
            },
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldButton,
            unfocusedBorderColor = MediumGreen,
            focusedTextColor = DarkText,
            unfocusedTextColor = DarkText,
            cursorColor = GoldButton
        )
    )
}

@Composable
fun AnimatedLoginButton(
    onClick: () -> Unit,
    text: String
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "login_button_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 8f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "login_button_elevation"
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
fun AnimatedTextButtonLogin(
    onClick: () -> Unit,
    text: String
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "text_button_login_scale"
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
