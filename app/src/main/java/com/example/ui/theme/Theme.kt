package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
  darkColorScheme(
    primary = GoldPrimary,
    onPrimary = TextDark,
    primaryContainer = GoldContainer,
    onPrimaryContainer = GoldLight,
    secondary = GoldMedium,
    onSecondary = TextDark,
    secondaryContainer = EmeraldContainer,
    onSecondaryContainer = GoldLight,
    tertiary = RubyJewel,
    onTertiary = TextIvory,
    background = RoyalBlack,
    onBackground = TextIvory,
    surface = RoyalBlack,
    onSurface = TextLight,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSand,
    outline = GoldBorder,
    outlineVariant = EmeraldBorder,
  )

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content,
  )
}
