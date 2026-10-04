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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
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
fun UnitConverterScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val categories = listOf("Length", "Weight", "Temperature", "Time", "Area", "Volume")
    var selectedCategory by remember { mutableStateOf("Length") }

    var inputValue by remember { mutableStateOf("10") }
    var fromUnit by remember { mutableStateOf("Meters") }
    var toUnit by remember { mutableStateOf("Kilometers") }

    val unitsForCategory = when (selectedCategory) {
        "Length" -> listOf("Meters", "Kilometers", "Centimeters", "Miles", "Feet", "Inches")
        "Weight" -> listOf("Kilograms", "Grams", "Pounds", "Ounces")
        "Temperature" -> listOf("Celsius", "Fahrenheit", "Kelvin")
        "Time" -> listOf("Seconds", "Minutes", "Hours", "Days")
        "Area" -> listOf("Sq Meters", "Sq Kilometers", "Hectares", "Acres")
        else -> listOf("Liters", "Milliliters", "Gallons", "Cubic Meters")
    }

    val convertedValue = try {
        val num = inputValue.toDoubleOrNull() ?: 0.0
        val res = convertUnits(num, fromUnit, toUnit, selectedCategory)
        "%.4f".format(res).trimEnd('0').trimEnd('.')
    } catch (e: Exception) {
        "0"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("unit_converter_screen")
    ) {
        MirajTopAppBar(
            title = "Unit Converter",
            subtitle = "Physics & Engineering Conversions",
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
            // Category Tabs
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSel = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) ElectricBlue.copy(alpha = 0.2f) else MirajSurface)
                                .border(1.dp, if (isSel) ElectricBlue else MirajBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedCategory = cat
                                    val newUnits = when (cat) {
                                        "Length" -> listOf("Meters", "Kilometers")
                                        "Weight" -> listOf("Kilograms", "Grams")
                                        "Temperature" -> listOf("Celsius", "Fahrenheit")
                                        "Time" -> listOf("Seconds", "Minutes")
                                        "Area" -> listOf("Sq Meters", "Hectares")
                                        else -> listOf("Liters", "Gallons")
                                    }
                                    fromUnit = newUnits[0]
                                    toUnit = newUnits[1]
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat,
                                color = if (isSel) ElectricBlue else TextSecondary,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Converter Input & Result Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(text = "From:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        // From Unit Selector
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(unitsForCategory) { u ->
                                val isSel = fromUnit == u
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PurpleAccent.copy(alpha = 0.25f) else MirajSurfaceElevated)
                                        .border(1.dp, if (isSel) PurpleAccent else MirajBorder, RoundedCornerShape(8.dp))
                                        .clickable { fromUnit = u }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = u, color = if (isSel) PurpleAccent else TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                        // Swap Button
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    val tmp = fromUnit
                                    fromUnit = toUnit
                                    toUnit = tmp
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MirajSurfaceElevated)
                                    .border(1.dp, MirajBorder, CircleShape)
                            ) {
                                Icon(Icons.Filled.SwapVert, contentDescription = "Swap", tint = ElectricBlue)
                            }
                        }

                        Text(text = "To:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        // To Unit Selector
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(unitsForCategory) { u ->
                                val isSel = toUnit == u
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) CyanAccent.copy(alpha = 0.25f) else MirajSurfaceElevated)
                                        .border(1.dp, if (isSel) CyanAccent else MirajBorder, RoundedCornerShape(8.dp))
                                        .clickable { toUnit = u }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = u, color = if (isSel) CyanAccent else TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        // Output Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(MirajSurfaceElevated)
                                .border(1.dp, CyanAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(text = "CONVERTED RESULT", color = CyanAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$convertedValue $toUnit",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

private fun convertUnits(value: Double, from: String, to: String, category: String): Double {
    if (from == to) return value
    return when (category) {
        "Length" -> {
            // Convert to meters first
            val inMeters = when (from) {
                "Meters" -> value
                "Kilometers" -> value * 1000
                "Centimeters" -> value * 0.01
                "Miles" -> value * 1609.34
                "Feet" -> value * 0.3048
                "Inches" -> value * 0.0254
                else -> value
            }
            when (to) {
                "Meters" -> inMeters
                "Kilometers" -> inMeters / 1000
                "Centimeters" -> inMeters * 100
                "Miles" -> inMeters / 1609.34
                "Feet" -> inMeters / 0.3048
                "Inches" -> inMeters / 0.0254
                else -> inMeters
            }
        }
        "Weight" -> {
            val inGrams = when (from) {
                "Kilograms" -> value * 1000
                "Grams" -> value
                "Pounds" -> value * 453.592
                "Ounces" -> value * 28.3495
                else -> value
            }
            when (to) {
                "Kilograms" -> inGrams / 1000
                "Grams" -> inGrams
                "Pounds" -> inGrams / 453.592
                "Ounces" -> inGrams / 28.3495
                else -> inGrams
            }
        }
        "Temperature" -> {
            val inCelsius = when (from) {
                "Celsius" -> value
                "Fahrenheit" -> (value - 32) * 5 / 9
                "Kelvin" -> value - 273.15
                else -> value
            }
            when (to) {
                "Celsius" -> inCelsius
                "Fahrenheit" -> (inCelsius * 9 / 5) + 32
                "Kelvin" -> inCelsius + 273.15
                else -> inCelsius
            }
        }
        "Time" -> {
            val inSec = when (from) {
                "Seconds" -> value
                "Minutes" -> value * 60
                "Hours" -> value * 3600
                "Days" -> value * 86400
                else -> value
            }
            when (to) {
                "Seconds" -> inSec
                "Minutes" -> inSec / 60
                "Hours" -> inSec / 3600
                "Days" -> inSec / 86400
                else -> inSec
            }
        }
        else -> value
    }
}
