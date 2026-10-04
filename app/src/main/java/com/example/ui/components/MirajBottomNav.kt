package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MirajBorder
import com.example.ui.theme.MirajSurface
import com.example.ui.theme.MirajSurfaceElevated
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextTertiary

data class NavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val BottomNavItems = listOf(
    NavItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem("chat", "AI Chat", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    NavItem("study", "Study Hub", Icons.Filled.School, Icons.Outlined.School),
    NavItem("tools", "Tools", Icons.Filled.Build, Icons.Outlined.Build),
    NavItem("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun MirajBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MirajSurface)
            .border(width = 1.dp, color = MirajBorder.copy(alpha = 0.5f))
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onNavigate(item.route) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("nav_item_${item.route}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) ElectricBlue.copy(alpha = 0.18f) else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = if (isSelected) ElectricBlue else TextTertiary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) TextPrimary else TextTertiary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
