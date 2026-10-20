package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PastelSkyBluePrimary,
    onPrimary = Color.White,
    primaryContainer = PastelSkyBlueContainer,
    onPrimaryContainer = OnPastelSkyBlueContainer,
    secondary = SageGreenSecondary,
    onSecondary = Color.White,
    secondaryContainer = SageGreenContainer,
    onSecondaryContainer = OnSageGreenContainer,
    tertiary = SoftLavenderTertiary,
    onTertiary = Color.White,
    tertiaryContainer = SoftLavenderContainer,
    onTertiaryContainer = OnSoftLavenderContainer,
    background = WarmCreamBackground,
    onBackground = DeepNavyText,
    surface = WarmIvorySurface,
    onSurface = DeepNavyText,
    surfaceVariant = WarmIvorySurfaceVariant,
    onSurfaceVariant = DeepNavyText,
    outline = WarmOutline,
    outlineVariant = Color(0xFFF0E8DC)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF82B1FF),
    onPrimary = Color(0xFF002B59),
    primaryContainer = Color(0xFF154884),
    onPrimaryContainer = Color(0xFFD3E4FF),
    secondary = Color(0xFF76D799),
    onSecondary = Color(0xFF00391A),
    secondaryContainer = Color(0xFF155331),
    onSecondaryContainer = Color(0xFFB8F3CC),
    tertiary = Color(0xFFCBB2FE),
    onTertiary = Color(0xFF381A72),
    tertiaryContainer = Color(0xFF50358E),
    onTertiaryContainer = Color(0xFFE9DCFF),
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF454B57)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
