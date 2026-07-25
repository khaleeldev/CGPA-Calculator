package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CgpaBottomNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        NavigationBarItem(
            selected = currentTab == AppTab.CALCULATOR,
            onClick = { onTabSelected(AppTab.CALCULATOR) },
            modifier = Modifier.testTag("nav_calculator"),
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.CALCULATOR) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                    contentDescription = "Calculator Tab"
                )
            },
            label = {
                Text(
                    text = "Calculator",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (currentTab == AppTab.CALCULATOR) FontWeight.Bold else FontWeight.Normal
                    )
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        NavigationBarItem(
            selected = currentTab == AppTab.HISTORY,
            onClick = { onTabSelected(AppTab.HISTORY) },
            modifier = Modifier.testTag("nav_history"),
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                    contentDescription = "History Tab"
                )
            },
            label = {
                Text(
                    text = "History",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (currentTab == AppTab.HISTORY) FontWeight.Bold else FontWeight.Normal
                    )
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        NavigationBarItem(
            selected = currentTab == AppTab.SETTINGS,
            onClick = { onTabSelected(AppTab.SETTINGS) },
            modifier = Modifier.testTag("nav_settings"),
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                    contentDescription = "Settings Tab"
                )
            },
            label = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (currentTab == AppTab.SETTINGS) FontWeight.Bold else FontWeight.Normal
                    )
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
