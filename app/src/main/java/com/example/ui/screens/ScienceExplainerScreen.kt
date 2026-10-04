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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScienceConcept
import com.example.ui.components.MirajTopAppBar
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
fun ScienceExplainerScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onAskInChat: (String) -> Unit
) {
    val selectedCategory by viewModel.selectedScienceCategory.collectAsState()
    val concepts by viewModel.scienceConcepts.collectAsState()

    var customQuestion by remember { mutableStateOf("") }
    val categories = listOf("Physics", "Chemistry", "Biology", "General Science")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("science_explainer_screen")
    ) {
        MirajTopAppBar(
            title = "Science Explainer",
            subtitle = "Intuitive STEM Fundamentals",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        // Subject Tabs
        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
            containerColor = MirajSurface,
            contentColor = ElectricBlue,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                val index = categories.indexOf(selectedCategory).coerceAtLeast(0)
                if (index < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[index]),
                        color = ElectricBlue
                    )
                }
            }
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                Tab(
                    selected = isSelected,
                    onClick = { viewModel.setScienceCategory(cat) },
                    text = {
                        Text(
                            text = cat,
                            color = if (isSelected) ElectricBlue else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Custom Question Box
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "Ask Any Science Question:",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customQuestion,
                            onValueChange = { customQuestion = it },
                            placeholder = { Text("e.g. Why is the sky blue? Or what is CRISPR?", color = TextTertiary) },
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
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                if (customQuestion.isNotBlank()) {
                                    val q = customQuestion
                                    customQuestion = ""
                                    onAskInChat("Explain this $selectedCategory topic in detail with examples: $q")
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryGradient)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Explain with Miraj AI",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Core Curriculum Concepts ($selectedCategory):",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 15.sp
                    )
                )
            }

            // Concepts List
            items(concepts, key = { it.id }) { concept ->
                ScienceConceptCard(
                    concept = concept,
                    onAskFollowup = { onAskInChat("Tell me more about ${concept.title} and give me numerical examples.") }
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun ScienceConceptCard(
    concept: ScienceConcept,
    onAskFollowup: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = concept.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextPrimary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TealAccent.copy(alpha = 0.18f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = concept.category,
                        color = TealAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1. Simple Explanation
            Text(
                text = "💡 Simple Explanation:",
                color = ElectricBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = concept.simpleExplanation,
                color = TextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Detailed Explanation
            Text(
                text = "🔬 Detailed Mechanism:",
                color = PurpleAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = concept.detailedExplanation,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Real-world Examples
            Text(
                text = "🌍 Real-World Examples:",
                color = EmeraldSuccess,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            concept.examples.forEach { ex ->
                Row(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "• ", color = EmeraldSuccess, fontSize = 13.sp)
                    Text(text = ex, color = TextSecondary, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Key Takeaways
            Text(
                text = "📌 Important Exam Points:",
                color = CyanAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            concept.keyTakeaways.forEach { point ->
                Row(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "✔ ", color = CyanAccent, fontSize = 12.sp)
                    Text(text = point, color = TextPrimary, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ask Followup Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MirajSurfaceElevated)
                        .border(1.dp, MirajBorder, RoundedCornerShape(10.dp))
                        .clickable(onClick = onAskFollowup)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Ask Follow-up in Chat →",
                        color = ElectricBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
