package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MirajDarkBg
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(700))
        scale.animateTo(1f, animationSpec = tween(700))
        delay(1200)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
                .padding(24.dp)
        ) {
            // Glowing Logo Box
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(PrimaryGradient)
                    .border(2.dp, CyanAccent.copy(alpha = 0.6f), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.School,
                    contentDescription = "Miraj AI Logo",
                    tint = Color.White,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "Miraj AI",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Your Smart Study & AI Assistant",
                color = ElectricBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tagline Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF131D33))
                    .border(1.dp, PurpleAccent.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Learn • Create • Grow",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
