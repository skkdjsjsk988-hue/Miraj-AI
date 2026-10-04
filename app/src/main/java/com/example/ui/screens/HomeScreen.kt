package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.FeatureCard
import com.example.ui.components.MirajTopAppBar
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MirajBorder
import com.example.ui.theme.MirajDarkBg
import com.example.ui.theme.MirajSurface
import com.example.ui.theme.MirajSurfaceCard
import com.example.ui.theme.MirajSurfaceElevated
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val userProfile by viewModel.repository.userProfile.collectAsState()
    var searchInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("home_screen")
    ) {
        // App Header
        MirajTopAppBar(
            title = "Good Morning, ${userProfile.name.substringBefore(" ")}",
            subtitle = "Miraj AI • Pro Assistant",
            showBackButton = false,
            showProBadge = true,
            onNotificationClick = { onNavigate("profile") }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Greeting
                Text(
                    text = "Good Morning 👋",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Text(
                    text = "What would you like to do today?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                )
            }

            // Large AI Search / Chat Input Box
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ElectricBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "AI",
                                tint = ElectricBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = searchInput,
                            onValueChange = { searchInput = it },
                            placeholder = {
                                Text(
                                    text = "Ask me anything...",
                                    color = TextTertiary,
                                    fontSize = 14.sp
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (searchInput.isNotBlank()) {
                                        viewModel.sendChatMessage(searchInput)
                                        searchInput = ""
                                        onNavigate("chat")
                                    }
                                }
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = ElectricBlue
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_ai_input")
                        )

                        // Mic button
                        IconButton(
                            onClick = { onNavigate("voice") },
                            modifier = Modifier.testTag("home_mic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "Voice Input",
                                tint = PurpleAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Send button
                        IconButton(
                            onClick = {
                                if (searchInput.isNotBlank()) {
                                    viewModel.sendChatMessage(searchInput)
                                    searchInput = ""
                                    onNavigate("chat")
                                } else {
                                    onNavigate("chat")
                                }
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryGradient)
                                .testTag("home_send_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Hero Banner & Daily Inspiration
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurfaceCard)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.miraj_hero_banner),
                        contentDescription = "Hero Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        MirajDarkBg.copy(alpha = 0.90f),
                                        MirajDarkBg.copy(alpha = 0.65f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricBlue.copy(alpha = 0.25f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "DAILY STUDY TIP",
                                color = ElectricBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "“Consistency beats intensity.”",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        Text(
                            text = "Try a 25-minute Pomodoro focus session today!",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatPill(
                        label = "Streak",
                        value = "${userProfile.streakDays} Days",
                        icon = Icons.Filled.LocalFireDepartment,
                        iconTint = AmberWarning,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "Studied",
                        value = "${userProfile.totalStudyHours}h",
                        icon = Icons.Filled.Timer,
                        iconTint = ElectricBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "Solved",
                        value = "${userProfile.questionsSolved}",
                        icon = Icons.Filled.CheckCircle,
                        iconTint = EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Feature Cards Section
            item {
                SectionHeader(
                    title = "Core Features",
                    subtitle = "AI tools crafted for high-performance learning"
                )
            }

            // Feature Cards - 2 Column Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Row 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeatureCard(
                            title = "AI Chat",
                            subtitle = "Chat with AI & solve doubts",
                            icon = Icons.Filled.AutoAwesome,
                            iconBrush = Brush.horizontalGradient(listOf(ElectricBlue, PurpleAccent)),
                            tag = "Instant",
                            onClick = { onNavigate("chat") },
                            modifier = Modifier.weight(1f)
                        )
                        FeatureCard(
                            title = "Study Hub",
                            subtitle = "Learn & Practice all subjects",
                            icon = Icons.Filled.School,
                            iconBrush = Brush.horizontalGradient(listOf(CyanAccent, TealAccent)),
                            tag = "Hub",
                            onClick = { onNavigate("study") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeatureCard(
                            title = "Maths Solver",
                            subtitle = "Step-by-step problem solver",
                            icon = Icons.Filled.Functions,
                            iconBrush = Brush.horizontalGradient(listOf(PurpleAccent, ElectricBlue)),
                            tag = "Steps",
                            onClick = { onNavigate("maths") },
                            modifier = Modifier.weight(1f)
                        )
                        FeatureCard(
                            title = "Science Explainer",
                            subtitle = "Physics, Chem, Bio concepts",
                            icon = Icons.Filled.Science,
                            iconBrush = Brush.horizontalGradient(listOf(EmeraldSuccess, TealAccent)),
                            tag = "Easy",
                            onClick = { onNavigate("science") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 3
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeatureCard(
                            title = "Notes Maker",
                            subtitle = "Create & summarize notes",
                            icon = Icons.Filled.EditNote,
                            iconBrush = Brush.horizontalGradient(listOf(AmberWarning, PurpleAccent)),
                            tag = "Saved",
                            onClick = { onNavigate("notes") },
                            modifier = Modifier.weight(1f)
                        )
                        FeatureCard(
                            title = "MCQ Generator",
                            subtitle = "Practice custom test MCQs",
                            icon = Icons.Filled.Quiz,
                            iconBrush = Brush.horizontalGradient(listOf(CyanAccent, ElectricBlue)),
                            tag = "Quiz",
                            onClick = { onNavigate("mcq") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Tools Shortcut
            item {
                SectionHeader(
                    title = "Quick Tools",
                    subtitle = "Essential student utilities",
                    actionText = "View All",
                    onActionClick = { onNavigate("tools") }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolItem(
                        title = "Calculator",
                        icon = Icons.Filled.Calculate,
                        color = ElectricBlue,
                        onClick = { onNavigate("calculator") },
                        modifier = Modifier.weight(1f)
                    )
                    QuickToolItem(
                        title = "Timer",
                        icon = Icons.Filled.Timer,
                        color = PurpleAccent,
                        onClick = { onNavigate("timer") },
                        modifier = Modifier.weight(1f)
                    )
                    QuickToolItem(
                        title = "Flashcards",
                        icon = Icons.Filled.School,
                        color = TealAccent,
                        onClick = { onNavigate("flashcards") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun QuickToolItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}
