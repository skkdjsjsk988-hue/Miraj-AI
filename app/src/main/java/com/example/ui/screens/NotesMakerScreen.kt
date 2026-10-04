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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyNote
import com.example.ui.components.MirajTopAppBar
import com.example.ui.components.PulseLoading
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesMakerScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.notesSearchQuery.collectAsState()
    val scope = rememberCoroutineScope()

    var showEditorDialog by remember { mutableStateOf(false) }
    var editingNoteId by remember { mutableStateOf<String?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editSubject by remember { mutableStateOf("Physics") }
    var editContent by remember { mutableStateOf("") }
    var isAiProcessing by remember { mutableStateOf(false) }

    val filteredNotes = notes.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.subject.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("notes_maker_screen")
    ) {
        MirajTopAppBar(
            title = "Notes Maker",
            subtitle = "AI-Assisted Study Notes & Flashpoints",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Search Bar & Add Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setNotesSearchQuery(it) },
                        placeholder = { Text("Search notes...", color = TextTertiary, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = TextTertiary)
                        },
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
                        modifier = Modifier
                            .weight(1f)
                            .testTag("notes_search_input")
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = {
                            editingNoteId = null
                            editTitle = ""
                            editSubject = "Physics"
                            editContent = ""
                            showEditorDialog = true
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PrimaryGradient)
                            .testTag("add_note_fab")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "New Note",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Notes Count
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saved Notes (${filteredNotes.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 15.sp
                        )
                    )
                }
            }

            // Notes List
            if (filteredNotes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.EditNote,
                                contentDescription = null,
                                tint = TextTertiary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "No notes found", color = TextSecondary, fontSize = 14.sp)
                            Text(text = "Tap + to create your first AI-enhanced study note!", color = TextTertiary, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                items(filteredNotes, key = { it.id }) { note ->
                    NoteItemCard(
                        note = note,
                        onEdit = {
                            editingNoteId = note.id
                            editTitle = note.title
                            editSubject = note.subject
                            editContent = note.content
                            showEditorDialog = true
                        },
                        onDelete = {
                            viewModel.deleteNote(note.id)
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    // Editor Dialog
    if (showEditorDialog) {
        AlertDialog(
            onDismissRequest = { if (!isAiProcessing) showEditorDialog = false },
            title = {
                Text(
                    text = if (editingNoteId == null) "Create New Note" else "Edit Note",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Note Title") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MirajSurface,
                            unfocusedContainerColor = MirajSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editSubject,
                        onValueChange = { editSubject = it },
                        label = { Text("Subject (e.g. Physics, Chemistry, Maths)") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MirajSurface,
                            unfocusedContainerColor = MirajSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text("Note Content & Formulas") },
                        maxLines = 6,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MirajSurface,
                            unfocusedContainerColor = MirajSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    )

                    if (isAiProcessing) {
                        PulseLoading(text = "Miraj AI is analyzing and updating notes...")
                    }

                    // AI Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // AI Summarize
                        Button(
                            onClick = {
                                if (editContent.isNotBlank() && !isAiProcessing) {
                                    scope.launch {
                                        isAiProcessing = true
                                        val summary = viewModel.summarizeNote(editContent)
                                        editContent = "$editContent\n\n📝 **AI Key Summary:**\n$summary"
                                        isAiProcessing = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MirajSurfaceElevated),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("✨ AI Summarize", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // AI Improve Notes
                        Button(
                            onClick = {
                                if (editContent.isNotBlank() && !isAiProcessing) {
                                    scope.launch {
                                        isAiProcessing = true
                                        val improved = viewModel.improveNote(editContent)
                                        editContent = improved
                                        isAiProcessing = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MirajSurfaceElevated),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🚀 AI Improve", color = PurpleAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            containerColor = MirajSurfaceElevated,
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editTitle.isNotBlank() && editContent.isNotBlank()) {
                            if (editingNoteId == null) {
                                viewModel.addNote(
                                    title = editTitle,
                                    subject = editSubject,
                                    content = editContent,
                                    tags = listOf(editSubject, "AI-Enhanced")
                                )
                            } else {
                                viewModel.updateNote(
                                    id = editingNoteId!!,
                                    title = editTitle,
                                    subject = editSubject,
                                    content = editContent,
                                    tags = listOf(editSubject, "AI-Enhanced")
                                )
                            }
                            showEditorDialog = false
                        }
                    }
                ) {
                    Text("Save Note", color = ElectricBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditorDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun NoteItemCard(
    note: StudyNote,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(note.updatedAt))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("note_card_${note.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AmberWarning.copy(alpha = 0.18f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = note.subject,
                        color = AmberWarning,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(30.dp)) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFF43F5E), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = note.content,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Updated: $dateStr",
                    color = TextTertiary,
                    fontSize = 11.sp
                )
                Text(
                    text = "Tap to expand",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    modifier = Modifier.clickable(onClick = onEdit)
                )
            }
        }
    }
}
