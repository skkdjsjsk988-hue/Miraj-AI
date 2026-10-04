package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.ui.theme.ProGold
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseError
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun QuizScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val questions by viewModel.activeQuizQuestions.collectAsState()
    val currentIndex by viewModel.currentQuizIndex.collectAsState()
    val selectedOption by viewModel.selectedOptionIndex.collectAsState()
    val isAnswerSubmitted by viewModel.isQuizAnswerSubmitted.collectAsState()
    val score by viewModel.quizScore.collectAsState()
    val isFinished by viewModel.isQuizFinished.collectAsState()

    val currentQ = questions.getOrNull(currentIndex)
    val totalQuestions = questions.size.coerceAtLeast(1)
    val progress = (currentIndex + 1).toFloat() / totalQuestions.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("quiz_screen")
    ) {
        MirajTopAppBar(
            title = "Interactive Quiz",
            subtitle = if (!isFinished) "Question ${currentIndex + 1} of $totalQuestions" else "Quiz Completed",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        if (!isFinished && currentQ != null) {
            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = ElectricBlue,
                trackColor = MirajSurfaceElevated
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // Question Header Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MirajSurface)
                            .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                            .padding(18.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ElectricBlue.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = currentQ.subject,
                                        color = ElectricBlue,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "Score: $score",
                                    color = ProGold,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentQ.question,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 17.sp,
                                    lineHeight = 24.sp
                                )
                            )
                        }
                    }
                }

                // 4 Options
                items(currentQ.options.size) { optionIndex ->
                    val optionText = currentQ.options[optionIndex]
                    val isSelected = selectedOption == optionIndex
                    val isCorrect = optionIndex == currentQ.correctOptionIndex

                    val borderColor = when {
                        !isAnswerSubmitted && isSelected -> ElectricBlue
                        isAnswerSubmitted && isCorrect -> EmeraldSuccess
                        isAnswerSubmitted && isSelected && !isCorrect -> RoseError
                        else -> MirajBorder
                    }

                    val bgColor = when {
                        !isAnswerSubmitted && isSelected -> ElectricBlue.copy(alpha = 0.15f)
                        isAnswerSubmitted && isCorrect -> EmeraldSuccess.copy(alpha = 0.15f)
                        isAnswerSubmitted && isSelected && !isCorrect -> RoseError.copy(alpha = 0.15f)
                        else -> MirajSurface
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                            .clickable {
                                if (!isAnswerSubmitted) {
                                    viewModel.selectQuizOption(optionIndex)
                                }
                            }
                            .padding(16.dp)
                            .testTag("quiz_option_$optionIndex")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val optionLetter = ('A' + optionIndex).toString()
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isAnswerSubmitted && isCorrect -> EmeraldSuccess
                                            isAnswerSubmitted && isSelected && !isCorrect -> RoseError
                                            isSelected -> ElectricBlue
                                            else -> MirajSurfaceElevated
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetter,
                                    color = if (isSelected || (isAnswerSubmitted && isCorrect)) Color.White else TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = optionText,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )

                            if (isAnswerSubmitted) {
                                if (isCorrect) {
                                    Icon(Icons.Filled.Check, contentDescription = "Correct", tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                                } else if (isSelected) {
                                    Icon(Icons.Filled.Close, contentDescription = "Wrong", tint = RoseError, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                // Answer Explanation if submitted
                if (isAnswerSubmitted) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MirajSurfaceElevated)
                                .border(1.dp, CyanAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(
                                    text = "💡 Explanation:",
                                    color = CyanAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQ.explanation,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                // Action Controls: Check / Next / Previous
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (currentIndex > 0) {
                            Button(
                                onClick = { viewModel.previousQuizQuestion() },
                                colors = ButtonDefaults.buttonColors(containerColor = MirajSurface),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Previous", color = TextPrimary)
                                }
                            }
                        }

                        if (!isAnswerSubmitted) {
                            Button(
                                onClick = { viewModel.submitQuizAnswer() },
                                enabled = selectedOption != null,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (selectedOption != null) PrimaryGradient else androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(MirajBorder, MirajBorder)))
                                    .testTag("quiz_submit_button")
                            ) {
                                Text("Check Answer", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.nextQuizQuestion() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(PrimaryGradient)
                                    .testTag("quiz_next_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (currentIndex < totalQuestions - 1) "Next Question" else "Finish Quiz",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        } else {
            // Quiz Completed Result Screen
            val percentage = (score * 100) / totalQuestions
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MirajSurface)
                        .border(1.dp, ElectricBlue.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(ProGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = ProGold,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Quiz Completed! 🎉",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (percentage >= 80) "Outstanding mastery! You crushed it!" else "Good effort! Keep practicing to master it.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Score Display
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MirajSurfaceElevated)
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$score / $totalQuestions",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricBlue
                            )
                            Text(
                                text = "$percentage% Accuracy",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (percentage >= 70) EmeraldSuccess else ProGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.restartQuiz() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PrimaryGradient)
                            .testTag("quiz_restart_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Restart Quiz", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
