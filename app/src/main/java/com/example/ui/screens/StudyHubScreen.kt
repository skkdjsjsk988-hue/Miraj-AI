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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MirajTopAppBar
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MirajBorder
import com.example.ui.theme.MirajDarkBg
import com.example.ui.theme.MirajSurface
import com.example.ui.theme.MirajSurfaceElevated
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun StudyHubScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("study_hub_screen")
    ) {
        MirajTopAppBar(
            title = "Study Hub",
            subtitle = "Better Learning, Brighter Future",
            showBackButton = false,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Inspirational Hub Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, ElectricBlue.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.School,
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Your Complete Academic Suite",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Curated learning workflows with AI assistance",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Cards requested in Study Hub prompt:
            // Notes Maker, MCQ Generator, Quiz, Study Timer, Question Practice, Flashcards, Study Planner
            item {
                StudyHubRowItem(
                    title = "Notes Maker",
                    subtitle = "Organize, summarize & expand class notes with AI",
                    icon = Icons.Filled.EditNote,
                    iconColor = AmberWarning,
                    badge = "Saved",
                    onClick = { onNavigate("notes") }
                )
            }

            item {
                StudyHubRowItem(
                    title = "MCQ Generator",
                    subtitle = "Create tailored multi-choice exams by topic & difficulty",
                    icon = Icons.Filled.Quiz,
                    iconColor = CyanAccent,
                    badge = "Practice",
                    onClick = { onNavigate("mcq") }
                )
            }

            item {
                StudyHubRowItem(
                    title = "Interactive Quiz",
                    subtitle = "Timed test simulation with instant feedback & scoring",
                    icon = Icons.Filled.Psychology,
                    iconColor = PurpleAccent,
                    badge = "Exam Mode",
                    onClick = { onNavigate("quiz") }
                )
            }

            item {
                StudyHubRowItem(
                    title = "Study Timer",
                    subtitle = "25m focus & 5m break Pomodoro rhythm for peak retention",
                    icon = Icons.Filled.Timer,
                    iconColor = ElectricBlue,
                    badge = "Focus",
                    onClick = { onNavigate("timer") }
                )
            }

            item {
                StudyHubRowItem(
                    title = "Question Practice",
                    subtitle = "Solve step-by-step Math & Science questions with AI",
                    icon = Icons.Filled.HelpOutline,
                    iconColor = TealAccent,
                    badge = "AI Tutor",
                    onClick = { onNavigate("maths") }
                )
            }

            item {
                StudyHubRowItem(
                    title = "Flashcards",
                    subtitle = "Interactive flip cards for rapid formula & law recall",
                    icon = Icons.Filled.School,
                    iconColor = EmeraldSuccess,
                    badge = "Recall",
                    onClick = { onNavigate("flashcards") }
                )
            }

            item {
                StudyHubRowItem(
                    title = "Study Planner",
                    subtitle = "Set daily goals, revision schedule & track weekly hours",
                    icon = Icons.Filled.CalendarMonth,
                    iconColor = ElectricBlue,
                    badge = "Plan",
                    onClick = { onNavigate("profile") }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StudyHubRowItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    badge: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag("hub_item_${title.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconColor.copy(alpha = 0.16f))
                    .border(0.5.dp, iconColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(iconColor.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = iconColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Open",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
