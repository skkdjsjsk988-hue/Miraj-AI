package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MirajBorder
import com.example.ui.theme.MirajSurface
import com.example.ui.theme.MirajSurfaceElevated
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.ProGold
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MirajTopAppBar(
    title: String = "Miraj AI",
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    showProBadge: Boolean = true,
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MirajSurface)
            .border(width = 1.dp, color = MirajBorder.copy(alpha = 0.5f))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("appbar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            } else {
                // Miraj AI Logo / Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.School,
                        contentDescription = "Miraj AI Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 17.sp
                        )
                    )
                    if (showProBadge) {
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
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Notification / Sparkle action
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MirajSurfaceElevated)
                    .testTag("appbar_notification_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = ElectricBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
