package com.example.melo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CineMeloColorScheme = lightColorScheme(
    primary = GoldButton,
    onPrimary = DarkText,
    secondary = MediumGreen,
    onSecondary = WhiteText,
    tertiary = LightBeige,
    onTertiary = DarkText,
    background = DarkGreen,
    onBackground = LightBeige,
    surface = MediumGreen,
    onSurface = LightBeige,
    surfaceVariant = LightBeige,
    onSurfaceVariant = DarkText
)

// Configuraciones de animación para toda la app
object CineMeloAnimations {
    val buttonScale = tween<Float>(
        durationMillis = 150,
        easing = FastOutSlowInEasing
    )

    val carouselScroll = tween<Float>(
        durationMillis = 800,
        easing = FastOutSlowInEasing
    )

    val cardHover = tween<Float>(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    )

    val fadeInOut = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
}

@Composable
fun MeloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Deshabilitamos colores dinámicos para usar nuestra paleta
    content: @Composable () -> Unit
) {
    val colorScheme = CineMeloColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
