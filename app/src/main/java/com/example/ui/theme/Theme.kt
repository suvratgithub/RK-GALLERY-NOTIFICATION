package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
  lightColorScheme(
    primary = DeepIndigo,
    onPrimary = Color.White,
    primaryContainer = IndigoLightContainer,
    onPrimaryContainer = DeepIndigo,
    secondary = SaffronAmber,
    onSecondary = Color.White,
    secondaryContainer = SaffronSoft,
    onSecondaryContainer = Color(0xFF9C4100),
    tertiary = CyberCyan,
    onTertiary = Color.White,
    tertiaryContainer = CyberCyanSoft,
    onTertiaryContainer = Color(0xFF004F58),
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
  )

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkIndigoPrimary,
    onPrimary = Color(0xFF10174D),
    primaryContainer = IndigoSurface,
    onPrimaryContainer = Color(0xFFE8EAF6),
    secondary = DarkAmberSecondary,
    onSecondary = Color(0xFF452100),
    secondaryContainer = Color(0xFF5D3200),
    onSecondaryContainer = Color(0xFFFFE0B2),
    tertiary = DarkCyanTertiary,
    onTertiary = Color(0xFF00363D),
    tertiaryContainer = Color(0xFF004F58),
    onTertiaryContainer = Color(0xFFB2EBF2),
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnBackground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
  )

@Composable
fun RKGalleryTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val extendedColors = if (darkTheme) DarkShopExtendedColors else LightShopExtendedColors

  CompositionLocalProvider(LocalShopColors provides extendedColors) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = AppTypography,
      content = content,
    )
  }
}

object ShopTheme {
  val colors: ShopExtendedColors
    @Composable get() = LocalShopColors.current
}
