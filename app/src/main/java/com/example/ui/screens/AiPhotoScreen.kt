package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.api.GeminiApiClient
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
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun AiPhotoScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var base64Image by remember { mutableStateOf<String?>(null) }
    var userQuestion by remember { mutableStateOf("Analyze this diagram and explain all key scientific concepts shown.") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var aiAnalysisResult by remember {
        mutableStateOf(
            """
🔍 **Multimodal Visual Analysis**

* **Subject Identified:** Scientific Schematic / Biological Cellular Concept
* **Key Observations:** The image depicts interconnected nodes resembling neural synaptic junctions and atomic valence orbitals.
* **Core Principles:** Energy transfer takes place across concentration gradients following thermodynamic laws.
* **Study Tip:** Practice drawing this diagram from memory to reinforce retention for exam questions!
            """.trimIndent()
        )
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val stream = context.contentResolver.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(stream)
                selectedBitmap = bmp
                val bos = ByteArrayOutputStream()
                bmp.compress(Bitmap.CompressFormat.JPEG, 75, bos)
                base64Image = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP)
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("ai_photo_screen")
    ) {
        MirajTopAppBar(
            title = "AI Photo Analysis",
            subtitle = "Vision Intelligence for Diagrams & Formulas",
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
            // Upload & Preview Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (selectedBitmap != null) {
                            Image(
                                bitmap = selectedBitmap!!.asImageBitmap(),
                                contentDescription = "Uploaded diagram",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        } else {
                            // Demo Sample Image
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MirajSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.miraj_hero_banner),
                                    contentDescription = "Sample diagram",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MirajDarkBg.copy(alpha = 0.75f))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Sample Educational Graphic Active",
                                        color = CyanAccent,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Upload Button
                        Button(
                            onClick = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MirajSurfaceElevated),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = ElectricBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Select New Image / Photo", color = ElectricBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Question Input
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
                            text = "Question About Image:",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = userQuestion,
                            onValueChange = { userQuestion = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MirajSurfaceElevated,
                                unfocusedContainerColor = MirajSurfaceElevated,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    isAnalyzing = true
                                    val res = GeminiApiClient.generateContent(
                                        prompt = userQuestion,
                                        imageBase64 = base64Image
                                    ).getOrNull()
                                    if (res != null && res.isNotBlank()) {
                                        aiAnalysisResult = res
                                    }
                                    isAnalyzing = false
                                }
                            },
                            enabled = !isAnalyzing,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryGradient)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isAnalyzing) "Analyzing with Vision AI..." else "Analyze with Miraj AI", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (isAnalyzing) {
                item {
                    PulseLoading(text = "Scanning pixels and computing visual explanation...")
                }
            }

            // Analysis Result Card
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
                        Text(
                            text = "AI VISION ANALYSIS",
                            color = CyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = aiAnalysisResult,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
