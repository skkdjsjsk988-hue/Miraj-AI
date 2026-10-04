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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun DictionaryScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val entry by viewModel.dictionaryEntry.collectAsState()
    var searchWord by remember { mutableStateOf("Hypothesis") }

    val popularAcademicWords = listOf("Entropy", "Hypothesis", "Catalyst", "Osmosis", "Resonance", "Equilibrium")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("dictionary_screen")
    ) {
        MirajTopAppBar(
            title = "Academic Dictionary",
            subtitle = "Terminology, Meanings & Phonetics",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Input
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchWord,
                        onValueChange = { searchWord = it },
                        placeholder = { Text("Look up word or term...", color = TextTertiary) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MirajSurface,
                            unfocusedContainerColor = MirajSurface,
                            focusedIndicatorColor = ElectricBlue,
                            unfocusedIndicatorColor = MirajBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = {
                            if (searchWord.isNotBlank()) {
                                viewModel.lookupWord(searchWord)
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PrimaryGradient)
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
                    }
                }
            }

            // Word Presets
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(popularAcademicWords) { w ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MirajSurfaceElevated)
                                .border(1.dp, MirajBorder, RoundedCornerShape(10.dp))
                                .clickable {
                                    searchWord = w
                                    viewModel.lookupWord(w)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = w, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Word Entry Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = entry.word,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${entry.phonetic} • ${entry.partOfSpeech}",
                                    color = ElectricBlue,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            IconButton(
                                onClick = { viewModel.speakText(entry.word) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue.copy(alpha = 0.2f))
                            ) {
                                Icon(Icons.Filled.VolumeUp, contentDescription = "Pronounce", tint = ElectricBlue, modifier = Modifier.size(20.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Definition
                        Text(
                            text = "Definition:",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = entry.definition,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Example
                        Text(
                            text = "Example in Context:",
                            color = EmeraldSuccess,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "“${entry.example}”",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Synonyms
                        Text(
                            text = "Academic Synonyms:",
                            color = PurpleAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.padding(top = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            entry.synonyms.forEach { syn ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PurpleAccent.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = syn, color = PurpleAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
