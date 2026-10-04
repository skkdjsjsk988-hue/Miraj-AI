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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MirajTopAppBar
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

@Composable
fun CalculatorScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val expression by viewModel.calculatorExpression.collectAsState()
    val result by viewModel.calculatorResult.collectAsState()

    val buttons = listOf(
        listOf("C", "(", ")", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "DEL", "=")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("calculator_screen")
    ) {
        MirajTopAppBar(
            title = "Scientific Calculator",
            subtitle = "Quick Calculations & Formulas",
            showBackButton = true,
            onBackClick = onNavigateBack,
            showProBadge = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display Screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MirajSurface)
                    .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (expression.isEmpty()) "0" else expression,
                        color = TextSecondary,
                        fontSize = 22.sp,
                        textAlign = TextAlign.End,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result,
                        color = TextPrimary,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Keypad
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                buttons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        row.forEach { key ->
                            val isAction = key in listOf("÷", "×", "-", "+", "=")
                            val isSpecial = key in listOf("C", "DEL", "(", ")")

                            val bgColor = when {
                                key == "=" -> ElectricBlue
                                isAction -> PurpleAccent.copy(alpha = 0.25f)
                                isSpecial -> MirajSurfaceElevated
                                else -> MirajSurface
                            }

                            val textColor = when {
                                key == "=" -> Color.White
                                isAction -> CyanAccent
                                isSpecial -> ElectricBlue
                                else -> TextPrimary
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(bgColor)
                                    .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
                                    .clickable { viewModel.onCalculatorKey(key) }
                                    .testTag("calc_key_$key"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = key,
                                    color = textColor,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
