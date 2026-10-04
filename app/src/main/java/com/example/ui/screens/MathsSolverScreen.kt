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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Lightbulb
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
fun MathsSolverScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentSolution by viewModel.currentMathSolution.collectAsState()
    val isSolving by viewModel.isMathSolving.collectAsState()
    val selectedCategory by viewModel.selectedMathCategory.collectAsState()

    var inputProblem by remember { mutableStateOf("2x + 3 = 11") }

    val categories = listOf("Algebra", "Arithmetic", "Equations", "Percentages", "Geometry", "Trigonometry")

    val sampleEquations = listOf(
        "2x + 3 = 11",
        "3x - 7 = 14",
        "x^2 - 5x + 6 = 0",
        "25% of 480",
        "sin^2(30) + cos^2(30)",
        "Area of circle with radius 7"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("maths_solver_screen")
    ) {
        MirajTopAppBar(
            title = "Maths Solver",
            subtitle = "Step-by-step Mathematical AI Engine",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Category Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ElectricBlue.copy(alpha = 0.2f) else MirajSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricBlue else MirajBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.setSelectedMathCategory(cat) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) ElectricBlue else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Math Input Box
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
                            text = "Enter Mathematical Equation or Problem:",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = inputProblem,
                            onValueChange = { inputProblem = it },
                            placeholder = { Text("e.g. 2x + 3 = 11", color = TextTertiary) },
                            singleLine = false,
                            maxLines = 3,
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
                                .testTag("math_problem_input")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Solve Button
                        Button(
                            onClick = {
                                viewModel.solveMath(inputProblem, selectedCategory)
                            },
                            enabled = !isSolving && inputProblem.isNotBlank(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(PrimaryGradient)
                                .testTag("math_solve_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Functions,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isSolving) "Solving Problem..." else "Solve Problem",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Quick Samples Chips
            item {
                Column {
                    Text(
                        text = "Quick Presets:",
                        color = TextTertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(sampleEquations) { eq ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MirajSurfaceElevated)
                                    .border(1.dp, MirajBorder, RoundedCornerShape(10.dp))
                                    .clickable {
                                        inputProblem = eq
                                        viewModel.solveMath(eq, selectedCategory)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = eq,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            if (isSolving) {
                item {
                    PulseLoading(text = "Calculating analytical derivation...")
                }
            }

            // Solution Display Area
            if (currentSolution != null) {
                val sol = currentSolution!!

                // Final Answer Card (Highlighted)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    listOf(ElectricBlue.copy(alpha = 0.25f), PurpleAccent.copy(alpha = 0.25f))
                                )
                            )
                            .border(1.dp, ElectricBlue.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                            .padding(18.dp)
                            .testTag("math_final_answer_card")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "FINAL ANSWER",
                                    color = ElectricBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = sol.finalAnswer,
                                    color = TextPrimary,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = sol.formulaOrMethod,
                                    color = CyanAccent,
                                    fontSize = 12.sp
                                )
                            }

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Answer", sol.finalAnswer)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Answer copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MirajSurface)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = "Copy Answer",
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Step-by-Step Breakdown
                item {
                    Text(
                        text = "Step-by-Step Derivation:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 16.sp
                        )
                    )
                }

                items(sol.steps.indices.toList()) { index ->
                    val stepText = sol.steps[index]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MirajSurface)
                            .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(PurpleAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = PurpleAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stepText,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
