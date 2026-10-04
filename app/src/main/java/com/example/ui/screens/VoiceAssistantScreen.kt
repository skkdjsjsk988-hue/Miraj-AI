package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.GeminiApiClient
import com.example.ui.components.MirajTopAppBar
import com.example.ui.components.PulseLoading
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
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun VoiceAssistantScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    var spokenQuery by remember { mutableStateOf("What are Newton's three laws of motion?") }
    var voiceResponse by remember {
        mutableStateOf(
            "Newton's laws explain how forces shape physical motion: First, inertia keeps bodies at constant velocity unless forced. Second, Force equals mass times acceleration (F = ma). Third, every action generates an equal and opposite reaction!"
        )
    }
    var isListening by remember { mutableStateOf(false) }
    var isThinking by remember { mutableStateOf(false) }

    // Android Speech Recognition contract
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isListening = false
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                spokenQuery = spoken
                scope.launch {
                    isThinking = true
                    val aiRes = GeminiApiClient.generateContent(
                        prompt = spoken,
                        language = selectedLanguage
                    ).getOrNull()
                    if (!aiRes.isNullOrBlank()) {
                        voiceResponse = aiRes
                        viewModel.speakText(aiRes)
                    }
                    isThinking = false
                }
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_mic")
    val micScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.25f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("voice_assistant_screen")
    ) {
        MirajTopAppBar(
            title = "Miraj AI Voice",
            subtitle = "Conversational Audio Study Assistant",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(10.dp)) }

            // Big Glowing Mic Avatar
            item {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .scale(micScale)
                        .clip(CircleShape)
                        .background(PrimaryGradient)
                        .border(3.dp, CyanAccent.copy(alpha = 0.6f), CircleShape)
                        .clickable {
                            try {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Miraj AI...")
                                }
                                isListening = true
                                speechLauncher.launch(intent)
                            } catch (e: Exception) {
                                isListening = false
                                // Fallback simulation
                                spokenQuery = "Explain how gravity works"
                                scope.launch {
                                    isThinking = true
                                    val aiRes = GeminiApiClient.generateContent(spokenQuery, language = selectedLanguage).getOrNull()
                                    if (!aiRes.isNullOrBlank()) {
                                        voiceResponse = aiRes
                                        viewModel.speakText(aiRes)
                                    }
                                    isThinking = false
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Mic,
                        contentDescription = "Tap to Speak",
                        tint = Color.White,
                        modifier = Modifier.size(68.dp)
                    )
                }
            }

            item {
                Text(
                    text = if (isListening) "Listening to your voice..." else "Tap Microphone to Speak",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (isListening) CyanAccent else TextPrimary
                )
            }

            if (isThinking) {
                item {
                    PulseLoading(text = "Miraj AI is synthesizing audio knowledge...")
                }
            }

            // User Speech Transcript
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "YOU ASKED:",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "“$spokenQuery”",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // AI Voice Response Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, ElectricBlue.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MIRAJ AI SPOKEN ANSWER",
                                color = ElectricBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            IconButton(
                                onClick = { viewModel.speakText(voiceResponse) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.VolumeUp,
                                    contentDescription = "Speak Again",
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = voiceResponse,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
