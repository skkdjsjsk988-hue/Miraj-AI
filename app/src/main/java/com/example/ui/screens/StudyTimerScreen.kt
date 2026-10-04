package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MirajTopAppBar
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MirajBorder
import com.example.ui.theme.MirajDarkBg
import com.example.ui.theme.MirajSurface
import com.example.ui.theme.MirajSurfaceElevated
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun StudyTimerScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val secondsLeft by viewModel.timerSecondsLeft.collectAsState()
    val totalSeconds by viewModel.timerDurationSeconds.collectAsState()
    val isRunning by viewModel.isTimerRunning.collectAsState()
    val isBreak by viewModel.isBreakMode.collectAsState()
    val completedSessions by viewModel.completedFocusSessions.collectAsState()

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timeFormatted = "%02d:%02d".format(minutes, seconds)
    val progress = if (totalSeconds > 0) (secondsLeft.toFloat() / totalSeconds.toFloat()) else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("study_timer_screen")
    ) {
        MirajTopAppBar(
            title = "Study Timer",
            subtitle = if (isBreak) "Break Time • Refresh Mind" else "Focus Mode • Deep Work",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mode Selector: Focus (25m) vs Break (5m)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MirajSurface)
                    .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // Focus tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isBreak) ElectricBlue.copy(alpha = 0.25f) else Color.Transparent)
                        .border(1.dp, if (!isBreak) ElectricBlue else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { viewModel.setTimerFocusMode() }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Timer, contentDescription = null, tint = if (!isBreak) ElectricBlue else TextSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "25m Focus",
                            color = if (!isBreak) TextPrimary else TextSecondary,
                            fontWeight = if (!isBreak) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }

                // Break tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isBreak) EmeraldSuccess.copy(alpha = 0.25f) else Color.Transparent)
                        .border(1.dp, if (isBreak) EmeraldSuccess else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { viewModel.setTimerBreakMode() }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Coffee, contentDescription = null, tint = if (isBreak) EmeraldSuccess else TextSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "5m Break",
                            color = if (isBreak) TextPrimary else TextSecondary,
                            fontWeight = if (isBreak) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Circular Timer Display
            Box(
                modifier = Modifier
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = MirajSurfaceElevated,
                    strokeWidth = 10.dp
                )

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = if (isBreak) EmeraldSuccess else ElectricBlue,
                    strokeWidth = 10.dp
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timeFormatted,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isBreak) "Rest & Rehydrate" else "Stay Locked In",
                        color = if (isBreak) EmeraldSuccess else CyanAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Session Stats Card
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MirajSurface)
                    .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Completed Focus Cycles: ",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "$completedSessions",
                        color = ElectricBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                Button(
                    onClick = { viewModel.resetTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = MirajSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Reset", tint = TextPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }

                // Start / Pause Button
                Button(
                    onClick = {
                        if (isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isRunning) androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(PurpleAccent, ElectricBlue)) else PrimaryGradient)
                        .testTag("timer_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRunning) "Pause" else "Start Focus",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
