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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun OcrScannerScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onSendToChat: (String) -> Unit,
    onSaveToNotes: (String) -> Unit
) {
    val context = LocalContext.current
    var isScanning by remember { mutableStateOf(false) }
    var extractedText by remember {
        mutableStateOf(
            "Law of Conservation of Momentum:\nIn an isolated system, the total linear momentum remains constant over time regardless of collisions between particles.\nEquation: m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂\nApplications: Rocket propulsion, recoil of a gun, billiard balls elastic collisions."
        )
    }

    val sampleDocuments = listOf(
        "Physics: Momentum & Collisions" to "Law of Conservation of Momentum:\nIn an isolated system, total momentum is conserved.\nm₁u₁ + m₂u₂ = m₁v₁ + m₂v₂\nRocket propulsion pushes mass downward to accelerate upward.",
        "Chemistry: Ideal Gas Law" to "Ideal Gas Equation:\nPV = nRT\nWhere P = pressure in Pa, V = volume in m³, n = moles of gas, R = 8.314 J/(mol·K), T = absolute temperature in Kelvin.",
        "Biology: Photosynthesis" to "Photosynthesis Reaction:\n6CO₂ + 6H₂O + Light Energy → C₆H₁₂O₆ + 6O₂\nChlorophyll in chloroplasts absorbs blue and red wavelengths."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("ocr_scanner_screen")
    ) {
        MirajTopAppBar(
            title = "OCR Text Scanner",
            subtitle = "Textbook Optical Character Recognition",
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
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(PurpleAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DocumentScanner,
                                contentDescription = null,
                                tint = PurpleAccent,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Scan Study Material & Formulas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )

                        Text(
                            text = "Extract printed or handwritten notes with AI precision",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                isScanning = true
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isScanning = false
                                    extractedText = sampleDocuments.random().second
                                    Toast.makeText(context, "Text extracted successfully!", Toast.LENGTH_SHORT).show()
                                }, 1000)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryGradient)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.UploadFile, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Scan Sample Document", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (isScanning) {
                item {
                    PulseLoading(text = "Scanning & parsing optical characters...")
                }
            }

            // Extracted Text Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, CyanAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "EXTRACTED STUDY TEXT",
                                color = CyanAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("OCR Text", extractedText)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Text copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = ElectricBlue, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = extractedText,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Actions: Send to Chat or Notes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    onSendToChat("Explain this extracted text in detail:\n$extractedText")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MirajSurfaceElevated),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Send to AI", color = ElectricBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = {
                                    onSaveToNotes(extractedText)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MirajSurfaceElevated),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.EditNote, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Save to Notes", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
