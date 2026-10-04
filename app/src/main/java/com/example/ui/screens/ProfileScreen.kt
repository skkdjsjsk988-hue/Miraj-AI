package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.ProGold
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.ThemeMode
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val selectedLang by viewModel.selectedLanguage.collectAsState()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLangDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirajDarkBg)
            .testTag("profile_screen")
    ) {
        MirajTopAppBar(
            title = "Profile & Settings",
            subtitle = "Academic Account & Preferences",
            showBackButton = false,
            showProBadge = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Profile Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(22.dp))
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(PrimaryGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "MA",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ProGold.copy(alpha = 0.2f))
                                        .border(0.5.dp, ProGold, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        color = ProGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = userProfile.email,
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Member since September 2026",
                                color = CyanAccent,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProfileStatCard(label = "Study Streak", value = "${userProfile.streakDays} Days", modifier = Modifier.weight(1f))
                    ProfileStatCard(label = "Total Hours", value = "${userProfile.totalStudyHours}h", modifier = Modifier.weight(1f))
                    ProfileStatCard(label = "Questions", value = "${userProfile.questionsSolved}", modifier = Modifier.weight(1f))
                }
            }

            // Settings Group
            item {
                Text(
                    text = "Preferences",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                ) {
                    SettingItem(
                        title = "Theme",
                        subtitle = when (themeMode) {
                            ThemeMode.DARK -> "Dark (Default)"
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.SYSTEM -> "System Default"
                        },
                        icon = Icons.Filled.DarkMode,
                        iconTint = PurpleAccent,
                        onClick = { showThemeDialog = true }
                    )
                    SettingItem(
                        title = "Language",
                        subtitle = selectedLang,
                        icon = Icons.Filled.Language,
                        iconTint = ElectricBlue,
                        onClick = { showLangDialog = true }
                    )
                    SettingItem(
                        title = "Notifications",
                        subtitle = "Study reminders & daily challenge",
                        icon = Icons.Filled.Notifications,
                        iconTint = CyanAccent,
                        onClick = { Toast.makeText(context, "Notifications enabled", Toast.LENGTH_SHORT).show() }
                    )
                }
            }

            item {
                Text(
                    text = "Security & Information",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MirajSurface)
                        .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
                ) {
                    SettingItem(
                        title = "Account & Security",
                        subtitle = "Password & two-factor authentication",
                        icon = Icons.Filled.Security,
                        iconTint = EmeraldSuccess,
                        onClick = { Toast.makeText(context, "Account secured with 2FA", Toast.LENGTH_SHORT).show() }
                    )
                    SettingItem(
                        title = "Privacy Policy",
                        subtitle = "Encrypted local device storage",
                        icon = Icons.Filled.Lock,
                        iconTint = PurpleAccent,
                        onClick = { Toast.makeText(context, "Your study data is encrypted & private", Toast.LENGTH_SHORT).show() }
                    )
                    SettingItem(
                        title = "About Miraj AI",
                        subtitle = "Version 1.0 • Learn • Create • Grow",
                        icon = Icons.Filled.Info,
                        iconTint = ElectricBlue,
                        onClick = { showAboutDialog = true }
                    )
                    SettingItem(
                        title = "Help & Support",
                        subtitle = "FAQs & Student Guides",
                        icon = Icons.Filled.Help,
                        iconTint = CyanAccent,
                        onClick = { Toast.makeText(context, "Support contact: mirajalam98917@gmail.com", Toast.LENGTH_LONG).show() }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    // Theme Mode Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Appearance Theme", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        ThemeMode.DARK to "Dark (Recommended)",
                        ThemeMode.LIGHT to "Light",
                        ThemeMode.SYSTEM to "System Follow"
                    ).forEach { (mode, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (themeMode == mode) ElectricBlue.copy(alpha = 0.2f) else MirajSurface)
                                .clickable {
                                    viewModel.setThemeMode(mode)
                                    showThemeDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                color = if (themeMode == mode) ElectricBlue else TextPrimary,
                                fontWeight = if (themeMode == mode) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            containerColor = MirajSurfaceElevated,
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Close", color = ElectricBlue)
                }
            }
        )
    }

    // Language Dialog
    if (showLangDialog) {
        AlertDialog(
            onDismissRequest = { showLangDialog = false },
            title = { Text("Select Learning Language", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("English", "Hindi", "Hinglish").forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedLang == lang) ElectricBlue.copy(alpha = 0.2f) else MirajSurface)
                                .clickable {
                                    viewModel.setSelectedLanguage(lang)
                                    showLangDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = lang,
                                color = if (selectedLang == lang) ElectricBlue else TextPrimary,
                                fontWeight = if (selectedLang == lang) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            containerColor = MirajSurfaceElevated,
            confirmButton = {
                TextButton(onClick = { showLangDialog = false }) {
                    Text("Close", color = ElectricBlue)
                }
            }
        )
    }

    // About Miraj AI Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About Miraj AI", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Miraj AI - Smart Study & Learning Assistant",
                        color = ElectricBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "“Learn • Create • Grow”",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Built for students to master Mathematics, Science, and STEM subjects through step-by-step problem solving, interactive testing, optical notes extraction, and multimodal AI intelligence.",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Version: 1.0 (Release Build)",
                        color = TextTertiary,
                        fontSize = 11.sp
                    )
                }
            },
            containerColor = MirajSurfaceElevated,
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK", color = ElectricBlue, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun ProfileStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(16.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
        }

        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(18.dp))
    }
}
