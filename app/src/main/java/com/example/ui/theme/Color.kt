package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Miraj AI Brand Colors
val MirajDarkBg = Color(0xFF080C14)
val MirajSurface = Color(0xFF0F172A)
val MirajSurfaceElevated = Color(0xFF151F38)
val MirajSurfaceCard = Color(0xFF131D33)
val MirajBorder = Color(0xFF1E2B48)
val MirajBorderHighlight = Color(0xFF384B75)

// Accents
val ElectricBlue = Color(0xFF3B82F6)
val ElectricBlueGlow = Color(0xFF60A5FA)
val PurpleAccent = Color(0xFF8B5CF6)
val PurpleGlow = Color(0xFFA78BFA)
val CyanAccent = Color(0xFF06B6D4)
val TealAccent = Color(0xFF14B8A6)
val EmeraldSuccess = Color(0xFF10B981)
val AmberWarning = Color(0xFFF59E0B)
val RoseError = Color(0xFFF43F5E)
val ProGold = Color(0xFFFFB800)

// Text
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF64748B)

// Gradients
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(ElectricBlue, PurpleAccent)
)

val GlowGradient = Brush.linearGradient(
    colors = listOf(ElectricBlue, PurpleAccent, CyanAccent)
)

val CardBorderGradient = Brush.linearGradient(
    colors = listOf(ElectricBlue.copy(alpha = 0.5f), PurpleAccent.copy(alpha = 0.2f), Color.Transparent)
)

val CardBackgroundBrush = Brush.verticalGradient(
    colors = listOf(MirajSurfaceElevated, MirajSurface)
)

val HeroHeaderGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF151D36), MirajDarkBg)
)
