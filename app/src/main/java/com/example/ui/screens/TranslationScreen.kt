package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun TranslationScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val input by viewModel.translationInput.collectAsState()
    val sourceLang by viewModel.sourceLanguage.collectAsState()
    val targetLang by viewModel.targetLanguage.collectAsState()
    val result by viewModel.translationResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("translation_screen")
    ) {
        MirajTopAppBar(
            title = "Academic Translation",
            subtitle = "Hindi ↔ English Language Tutor",
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
            // Language Swap Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sourceLang,
                        color = ElectricBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    IconButton(
                        onClick = { viewModel.swapTranslationLanguages() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MirajSurfaceElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SwapHoriz,
                            contentDescription = "Swap Languages",
                            tint = CyanAccent
                        )
                    }

                    Text(
                        text = targetLang,
                        color = PurpleAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            // Input Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Enter text to translate:",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = input,
                            onValueChange = { viewModel.setTranslationInput(it) },
                            maxLines = 5,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MirajSurfaceElevated,
                                unfocusedContainerColor = MirajSurfaceElevated,
                                focusedIndicatorColor = ElectricBlue,
                                unfocusedIndicatorColor = MirajBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = ElectricBlue
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.translateText() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryGradient)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Language, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Translate Text", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Result Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, EmeraldSuccess.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TRANSLATION ($targetLang)",
                                color = EmeraldSuccess,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Row {
                                IconButton(
                                    onClick = { viewModel.speakText(result) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Filled.VolumeUp, contentDescription = "Speak", tint = ElectricBlue, modifier = Modifier.size(18.dp))
                                }
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Translation", result)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Translation copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = ElectricBlue, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = result,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
