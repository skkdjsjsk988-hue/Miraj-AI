package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MirajBorder
import com.example.ui.theme.MirajBorderHighlight
import com.example.ui.theme.MirajSurface
import com.example.ui.theme.MirajSurfaceElevated
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun FeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBrush: Brush = PrimaryGradient,
    tag: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag("feature_card_${title.lowercase().replace(" ", "_")}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconBrush),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (tag != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElectricBlue.copy(alpha = 0.15f))
                            .border(0.5.dp, ElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = tag,
                            color = ElectricBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

@Composable
fun ToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag("tool_card_${title.lowercase().replace(" ", "_")}")
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.18f))
                    .border(0.5.dp, iconColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 14.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MirajSurface)
            .border(1.dp, MirajBorder, RoundedCornerShape(cornerRadius))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = ButtonDefaults.ContentPadding,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (enabled) PrimaryGradient else Brush.horizontalGradient(listOf(MirajBorder, MirajBorder)))
            .testTag("gradient_button_${text.lowercase().replace(" ", "_")}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PulseLoading(
    text: String = "Miraj AI is thinking...",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MirajSurfaceElevated)
            .border(1.dp, MirajBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(ElectricBlue)
                .alpha(alpha)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 17.sp
                )
            )
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

        if (actionText != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = actionText,
                    color = ElectricBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
