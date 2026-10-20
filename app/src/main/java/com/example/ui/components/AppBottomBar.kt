package com.example.ui.components

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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.Screen

@Composable
fun AppBottomBar(
    currentScreen: Screen,
    onNavigateTo: (Screen) -> Unit,
    onOpenMoreMenu: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        val isHome = currentScreen is Screen.Home
        val isDevotional = currentScreen is Screen.DailyDevotional
        val isStories = currentScreen is Screen.StoriesList || currentScreen is Screen.StoryDetail
        val isPlan = currentScreen is Screen.ThirtyDayPlan
        val isOther = !isHome && !isDevotional && !isStories && !isPlan

        NavigationBarItem(
            selected = isHome,
            onClick = { onNavigateTo(Screen.Home) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Início"
                )
            },
            label = { Text("Início") },
            modifier = Modifier.testTag("nav_item_home"),
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationBarItem(
            selected = isDevotional,
            onClick = { onNavigateTo(Screen.DailyDevotional) },
            icon = {
                Icon(
                    imageVector = if (isDevotional) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                    contentDescription = "Devocional"
                )
            },
            label = { Text("Devocional") },
            modifier = Modifier.testTag("nav_item_devotional"),
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationBarItem(
            selected = isStories,
            onClick = { onNavigateTo(Screen.StoriesList) },
            icon = {
                Icon(
                    imageVector = if (isStories) Icons.Filled.Book else Icons.Outlined.Book,
                    contentDescription = "Histórias"
                )
            },
            label = { Text("Histórias") },
            modifier = Modifier.testTag("nav_item_stories"),
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationBarItem(
            selected = isPlan,
            onClick = { onNavigateTo(Screen.ThirtyDayPlan) },
            icon = {
                Icon(
                    imageVector = if (isPlan) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                    contentDescription = "30 Dias"
                )
            },
            label = { Text("30 Dias") },
            modifier = Modifier.testTag("nav_item_plan"),
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationBarItem(
            selected = isOther,
            onClick = onOpenMoreMenu,
            icon = {
                Icon(
                    imageVector = if (isOther) Icons.Filled.Menu else Icons.Outlined.Menu,
                    contentDescription = "Mais"
                )
            },
            label = { Text("Mais") },
            modifier = Modifier.testTag("nav_item_more"),
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
