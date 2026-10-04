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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.components.MirajTopAppBar
import com.example.ui.components.PulseLoading
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
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun ChatScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isChatLoading.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val attachedImageBase64 by viewModel.attachedImageBase64.collectAsState()

    var textInput by remember { mutableStateOf("") }
    var showClearDialog by remember { mutableStateOf(false) }
    var attachedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val listState = rememberLazyListState()

    // Photo picker launcher (Zero-permission Android Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                attachedBitmap = bitmap
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
                val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
                viewModel.setAttachedImage(base64String)
            } catch (e: Exception) {
                // handle error gracefully
            }
        }
    }

    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("chat_screen")
    ) {
        // App Bar
        MirajTopAppBar(
            title = "Miraj AI Chat",
            subtitle = "Powered by Gemini Intelligence",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true,
            onNotificationClick = { showClearDialog = true }
        )

        // Language Selector Row & Clear Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MirajSurface)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("English", "Hindi", "Hinglish").forEach { lang ->
                    val isSelected = selectedLanguage.equals(lang, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) ElectricBlue.copy(alpha = 0.2f) else Color.Transparent)
                            .border(
                                1.dp,
                                if (isSelected) ElectricBlue else MirajBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.setSelectedLanguage(lang) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = lang,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) ElectricBlue else TextSecondary
                        )
                    }
                }
            }

            IconButton(
                onClick = { showClearDialog = true },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline,
                    contentDescription = "Clear Chat",
                    tint = TextTertiary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Preset Quick Question Chips if chat is young
            if (messages.size <= 2) {
                item {
                    Column {
                        Text(
                            text = "💡 Try asking:",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val samplePrompts = listOf(
                                "Explain Photosynthesis 🌱",
                                "Solve 2x + 3 = 11 📐",
                                "Newton's 3 Laws 🍎",
                                "DNA vs RNA 🧬",
                                "Thermodynamics Carnot Cycle ⚙️"
                            )
                            items(samplePrompts) { prompt ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MirajSurface)
                                        .border(1.dp, MirajBorder, RoundedCornerShape(12.dp))
                                        .clickable {
                                            viewModel.sendChatMessage(prompt)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = prompt,
                                        color = TextPrimary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                ChatBubble(
                    message = msg,
                    onSpeak = { viewModel.speakText(msg.text) }
                )
            }

            if (isLoading) {
                item {
                    PulseLoading(text = "Miraj AI is researching & formulating answer...")
                }
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }
        }

        // Attached image preview if any
        if (attachedBitmap != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MirajSurface)
                        .border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = attachedBitmap!!.asImageBitmap(),
                        contentDescription = "Attached photo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Image attached for AI analysis",
                        color = CyanAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            attachedBitmap = null
                            viewModel.setAttachedImage(null)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove image",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Bottom Input Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MirajSurface)
                .border(1.dp, MirajBorder)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photo Attachment Button
                IconButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.testTag("chat_photo_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddPhotoAlternate,
                        contentDescription = "Attach Photo",
                        tint = CyanAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Text Field
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = {
                        Text(
                            text = "Ask questions or describe photo...",
                            color = TextTertiary,
                            fontSize = 14.sp
                        )
                    },
                    maxLines = 3,
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
                        .testTag("chat_text_input")
                )

                // Voice Mic Button
                IconButton(
                    onClick = { onNavigate("voice") },
                    modifier = Modifier.testTag("chat_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Mic,
                        contentDescription = "Voice",
                        tint = PurpleAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Send Button
                IconButton(
                    onClick = {
                        if (textInput.isNotBlank() || attachedBitmap != null) {
                            val sendingText = textInput
                            textInput = ""
                            attachedBitmap = null
                            viewModel.sendChatMessage(sendingText)
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PrimaryGradient)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Clear Chat Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(text = "Clear Chat History", color = TextPrimary) },
            text = { Text(text = "Are you sure you want to clear current chat messages?", color = TextSecondary) },
            containerColor = MirajSurfaceElevated,
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearChat()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear", color = ElectricBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    onSpeak: () -> Unit
) {
    val isUser = message.isUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PrimaryGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.School,
                    contentDescription = "Miraj AI",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .background(
                    if (isUser) ElectricBlue.copy(alpha = 0.22f) else MirajSurface
                )
                .border(
                    1.dp,
                    if (isUser) ElectricBlue.copy(alpha = 0.45f) else MirajBorder,
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onSpeak,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VolumeUp,
                                contentDescription = "Read Aloud",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PurpleAccent.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "User",
                    tint = PurpleAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
