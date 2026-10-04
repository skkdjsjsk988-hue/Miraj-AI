package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.MirajTopAppBar
import com.example.ui.components.ToolCard
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MirajDarkBg
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseError
import com.example.ui.theme.TealAccent
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ToolsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("tools_screen")
    ) {
        MirajTopAppBar(
            title = "Academic Tools",
            subtitle = "Smart AI Utilities & Calculators",
            showBackButton = false,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Row 1: Calculator & Unit Converter
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToolCard(
                        title = "Calculator",
                        subtitle = "Scientific & arithmetic functions",
                        icon = Icons.Filled.Calculate,
                        iconColor = ElectricBlue,
                        onClick = { onNavigate("calculator") },
                        modifier = Modifier.weight(1f)
                    )
                    ToolCard(
                        title = "Unit Converter",
                        subtitle = "Length, weight, temp, time & area",
                        icon = Icons.Filled.Straighten,
                        iconColor = CyanAccent,
                        onClick = { onNavigate("converter") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 2: OCR Scanner & Dictionary
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToolCard(
                        title = "OCR Scanner",
                        subtitle = "Extract text from study books",
                        icon = Icons.Filled.DocumentScanner,
                        iconColor = PurpleAccent,
                        onClick = { onNavigate("ocr") },
                        modifier = Modifier.weight(1f)
                    )
                    ToolCard(
                        title = "Dictionary",
                        subtitle = "Meanings, phonetics & synonyms",
                        icon = Icons.Filled.MenuBook,
                        iconColor = AmberWarning,
                        onClick = { onNavigate("dictionary") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 3: Translation & Web Search
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToolCard(
                        title = "Translation",
                        subtitle = "Hindi ↔ English academic language",
                        icon = Icons.Filled.Language,
                        iconColor = EmeraldSuccess,
                        onClick = { onNavigate("translation") },
                        modifier = Modifier.weight(1f)
                    )
                    ToolCard(
                        title = "Web Knowledge",
                        subtitle = "Academic research & facts",
                        icon = Icons.Filled.Search,
                        iconColor = TealAccent,
                        onClick = { onNavigate("chat") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 4: AI Photo & Voice Assistant
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToolCard(
                        title = "AI Photo",
                        subtitle = "Multimodal photo & diagram review",
                        icon = Icons.Filled.CameraAlt,
                        iconColor = RoseError,
                        onClick = { onNavigate("photo") },
                        modifier = Modifier.weight(1f)
                    )
                    ToolCard(
                        title = "Voice & AI Voice",
                        subtitle = "Speech-to-text & audio explanation",
                        icon = Icons.Filled.Mic,
                        iconColor = PurpleAccent,
                        onClick = { onNavigate("voice") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
