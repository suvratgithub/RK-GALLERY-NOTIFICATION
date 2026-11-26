package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand Core Palette
val DeepIndigo = Color(0xFF1A237E)
val IndigoSurface = Color(0xFF283593)
val IndigoLightContainer = Color(0xFFE8EAF6)
val SaffronAmber = Color(0xFFFF6F00)
val SaffronSoft = Color(0xFFFFF3E0)
val CyberCyan = Color(0xFF0097A7)
val CyberCyanSoft = Color(0xFFE0F7FA)
val EmeraldGreen = Color(0xFF1B8A4A)
val WhatsAppGreen = Color(0xFF25D366)
val WhatsAppDark = Color(0xFF128C7E)
val InstagramPink = Color(0xFFE1306C)
val InstagramPurple = Color(0xFF833AB4)
val InstagramOrange = Color(0xFFF77737)
val StarGold = Color(0xFFFFC107)
val LiveRed = Color(0xFFE53935)
val TickerDark = Color(0xFF111B38)

// Light Theme Neutrals
val LightBackground = Color(0xFFF5F7FF)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFEAEFF9)
val LightOnBackground = Color(0xFF1B1E2B)
val LightOnSurfaceVariant = Color(0xFF5A6275)
val LightOutline = Color(0xFFD6DCEB)

// Dark Theme Palette
val DarkBackground = Color(0xFF0E1324)
val DarkSurface = Color(0xFF161D33)
val DarkSurfaceVariant = Color(0xFF222B47)
val DarkIndigoPrimary = Color(0xFF9FA8DA)
val DarkAmberSecondary = Color(0xFFFFB74D)
val DarkCyanTertiary = Color(0xFF4DD0E1)
val DarkOnBackground = Color(0xFFEAEFF9)
val DarkOnSurfaceVariant = Color(0xFFB3BCD1)
val DarkOutline = Color(0xFF333E5E)

@Immutable
data class ShopExtendedColors(
  val whatsapp: Color,
  val whatsappDark: Color,
  val cyberCyan: Color,
  val cyberCyanContainer: Color,
  val emerald: Color,
  val emeraldContainer: Color,
  val amber: Color,
  val amberContainer: Color,
  val instagramStart: Color,
  val instagramMid: Color,
  val instagramEnd: Color,
  val starGold: Color,
  val tickerBackground: Color,
  val liveRed: Color,
)

val LightShopExtendedColors =
  ShopExtendedColors(
    whatsapp = WhatsAppGreen,
    whatsappDark = WhatsAppDark,
    cyberCyan = CyberCyan,
    cyberCyanContainer = CyberCyanSoft,
    emerald = EmeraldGreen,
    emeraldContainer = Color(0xFFE8F5E9),
    amber = SaffronAmber,
    amberContainer = SaffronSoft,
    instagramStart = InstagramPurple,
    instagramMid = InstagramPink,
    instagramEnd = InstagramOrange,
    starGold = StarGold,
    tickerBackground = TickerDark,
    liveRed = LiveRed,
  )

val DarkShopExtendedColors =
  ShopExtendedColors(
    whatsapp = WhatsAppGreen,
    whatsappDark = WhatsAppDark,
    cyberCyan = DarkCyanTertiary,
    cyberCyanContainer = Color(0xFF004D56),
    emerald = Color(0xFF66BB6A),
    emeraldContainer = Color(0xFF133D22),
    amber = DarkAmberSecondary,
    amberContainer = Color(0xFF4A2800),
    instagramStart = InstagramPurple,
    instagramMid = InstagramPink,
    instagramEnd = InstagramOrange,
    starGold = StarGold,
    tickerBackground = Color(0xFF090D1A),
    liveRed = Color(0xFFEF5350),
  )

val LocalShopColors = staticCompositionLocalOf { LightShopExtendedColors }
