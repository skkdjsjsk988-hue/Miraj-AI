package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun FlashcardsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val flashcards by viewModel.flashcards.collectAsState()
    val currentIndex by viewModel.currentFlashcardIndex.collectAsState()
    val isFlipped by viewModel.isCardFlipped.collectAsState()

    val currentCard = flashcards.getOrNull(currentIndex)
    val totalCards = flashcards.size.coerceAtLeast(1)

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500),
        label = "card_flip"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("flashcards_screen")
    ) {
        MirajTopAppBar(
            title = "Study Flashcards",
            subtitle = "Active Recall Practice • Card ${currentIndex + 1} of $totalCards",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Subject & Mastered Status
            if (currentCard != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElectricBlue.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentCard.subject,
                            color = ElectricBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.toggleCurrentCardMastered() }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Mastered",
                            tint = if (currentCard.isMastered) EmeraldSuccess else TextTertiary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentCard.isMastered) "Mastered" else "Mark Mastered",
                            color = if (currentCard.isMastered) EmeraldSuccess else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // 3D Flippable Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 20.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            if (rotation <= 90f) MirajSurface else MirajSurfaceElevated
                        )
                        .border(
                            1.5.dp,
                            if (rotation <= 90f) MirajBorder else CyanAccent.copy(alpha = 0.6f),
                            RoundedCornerShape(24.dp)
                        )
                        .clickable { viewModel.flipCard() }
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // Front (Question / Concept)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(PurpleAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.School,
                                    contentDescription = null,
                                    tint = PurpleAccent,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = "QUESTION / TOPIC",
                                color = TextTertiary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = currentCard.front,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 19.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 26.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Flip, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tap to reveal answer",
                                    color = ElectricBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else {
                        // Back (Answer / Solution) - mirror rotated back
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.graphicsLayer { rotationY = 180f }
                        ) {
                            Text(
                                text = "CORE DEFINITION & FORMULA",
                                color = CyanAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = currentCard.back,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Tap to flip back",
                                color = TextTertiary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Controls: Previous / Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.previousFlashcard() },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MirajSurface)
                            .border(1.dp, MirajBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Previous Card",
                            tint = TextPrimary
                        )
                    }

                    Text(
                        text = "${currentIndex + 1} / $totalCards",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = { viewModel.nextFlashcard() },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(PrimaryGradient)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Next Card",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
