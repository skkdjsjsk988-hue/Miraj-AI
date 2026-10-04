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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun McqGeneratorScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    var subject by remember { mutableStateOf("Physics") }
    var topic by remember { mutableStateOf("Laws of Motion & Gravity") }
    var questionCount by remember { mutableStateOf(5) }
    var difficulty by remember { mutableStateOf("Medium") }
    var language by remember { mutableStateOf("English") }

    val subjects = listOf("Physics", "Chemistry", "Biology", "Mathematics")
    val difficulties = listOf("Easy", "Medium", "Hard")
    val counts = listOf(3, 5, 10)
    val languages = listOf("English", "Hindi", "Hinglish")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("mcq_generator_screen")
    ) {
        MirajTopAppBar(
            title = "MCQ Generator",
            subtitle = "Custom Exam Simulation Engine",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Config Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Configure Practice Test:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )

                        // 1. Subject Selector
                        Column {
                            Text(text = "Subject", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(subjects) { sub ->
                                    val isSel = subject == sub
                                    ChipOption(
                                        text = sub,
                                        isSelected = isSel,
                                        onClick = { subject = sub }
                                    )
                                }
                            }
                        }

                        // 2. Topic Input
                        Column {
                            Text(text = "Topic / Chapter", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = topic,
                                onValueChange = { topic = it },
                                placeholder = { Text("e.g. Thermodynamics, Optics, Cell Division", color = TextTertiary) },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MirajSurfaceElevated,
                                    unfocusedContainerColor = MirajSurfaceElevated,
                                    focusedIndicatorColor = ElectricBlue,
                                    unfocusedIndicatorColor = MirajBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    cursorColor = ElectricBlue
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // 3. Difficulty
                        Column {
                            Text(text = "Difficulty Level", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                difficulties.forEach { diff ->
                                    ChipOption(
                                        text = diff,
                                        isSelected = difficulty == diff,
                                        onClick = { difficulty = diff }
                                    )
                                }
                            }
                        }

                        // 4. Question Count
                        Column {
                            Text(text = "Number of Questions", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                counts.forEach { cnt ->
                                    ChipOption(
                                        text = "$cnt Questions",
                                        isSelected = questionCount == cnt,
                                        onClick = { questionCount = cnt }
                                    )
                                }
                            }
                        }

                        // 5. Language
                        Column {
                            Text(text = "Language", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                languages.forEach { lang ->
                                    ChipOption(
                                        text = lang,
                                        isSelected = language == lang,
                                        onClick = { language = lang }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Start Quiz Button
                        Button(
                            onClick = {
                                viewModel.startQuizWithConfig(subject, topic, questionCount, difficulty)
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(PrimaryGradient)
                                .testTag("start_quiz_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Start Interactive Quiz",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun ChipOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) ElectricBlue.copy(alpha = 0.2f) else MirajSurfaceElevated)
            .border(
                1.dp,
                if (isSelected) ElectricBlue else MirajBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) ElectricBlue else TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
        )
    }
}
