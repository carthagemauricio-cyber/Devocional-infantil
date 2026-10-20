package com.example.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.Screen

@Composable
fun AppNavigationRail(
    currentScreen: Screen,
    onNavigateTo: (Screen) -> Unit,
    onOpenMoreMenu: () -> Unit
) {
    val isHome = currentScreen is Screen.Home
    val isDevotional = currentScreen is Screen.DailyDevotional
    val isStories = currentScreen is Screen.StoriesList || currentScreen is Screen.StoryDetail
    val isPlan = currentScreen is Screen.ThirtyDayPlan
    val isOther = !isHome && !isDevotional && !isStories && !isPlan

    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surface,
        header = {
            Text(
                text = "✝️",
                fontSize = 28.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        NavigationRailItem(
            selected = isHome,
            onClick = { onNavigateTo(Screen.Home) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Início"
                )
            },
            label = { Text("Início") },
            modifier = Modifier.testTag("rail_item_home"),
            colors = NavigationRailItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationRailItem(
            selected = isDevotional,
            onClick = { onNavigateTo(Screen.DailyDevotional) },
            icon = {
                Icon(
                    imageVector = if (isDevotional) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                    contentDescription = "Devocional"
                )
            },
            label = { Text("Devocional") },
            modifier = Modifier.testTag("rail_item_devotional"),
            colors = NavigationRailItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationRailItem(
            selected = isStories,
            onClick = { onNavigateTo(Screen.StoriesList) },
            icon = {
                Icon(
                    imageVector = if (isStories) Icons.Filled.Book else Icons.Outlined.Book,
                    contentDescription = "Histórias"
                )
            },
            label = { Text("Histórias") },
            modifier = Modifier.testTag("rail_item_stories"),
            colors = NavigationRailItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationRailItem(
            selected = isPlan,
            onClick = { onNavigateTo(Screen.ThirtyDayPlan) },
            icon = {
                Icon(
                    imageVector = if (isPlan) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                    contentDescription = "30 Dias"
                )
            },
            label = { Text("30 Dias") },
            modifier = Modifier.testTag("rail_item_plan"),
            colors = NavigationRailItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationRailItem(
            selected = isOther,
            onClick = onOpenMoreMenu,
            icon = {
                Icon(
                    imageVector = if (isOther) Icons.Filled.Menu else Icons.Outlined.Menu,
                    contentDescription = "Mais"
                )
            },
            label = { Text("Mais") },
            modifier = Modifier.testTag("rail_item_more"),
            colors = NavigationRailItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
