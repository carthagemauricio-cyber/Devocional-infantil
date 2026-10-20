package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppNavigationRail
import com.example.ui.components.AppTopBar
import com.example.ui.components.MoreMenuSheet
import com.example.ui.screens.ActivitiesScreen
import com.example.ui.screens.AiPersonalizedScreen
import com.example.ui.screens.DailyDevotionalScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JesusTeachingsScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.PrayersScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StoryDetailScreen
import com.example.ui.screens.StoryListScreen
import com.example.ui.screens.ThirtyDayPlanScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentProfile by viewModel.currentProfile.collectAsState()
    var showMoreMenu by remember { mutableStateOf(false) }

    // Android Hardware / Gesture Back Handling
    BackHandler(enabled = currentScreen !is Screen.Home) {
        viewModel.navigateBack()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTabletOrWide = maxWidth >= 720.dp

        if (isTabletOrWide) {
            // Responsive Tablet / Wide Layout: Navigation Rail on left, content on right
            Row(modifier = Modifier.fillMaxSize()) {
                AppNavigationRail(
                    currentScreen = currentScreen,
                    onNavigateTo = { viewModel.navigateTo(it) },
                    onOpenMoreMenu = { showMoreMenu = true }
                )

                Scaffold(
                    modifier = Modifier.weight(1f),
                    topBar = {
                        AppTopBar(
                            currentScreen = currentScreen,
                            currentProfile = currentProfile,
                            onNavigateBack = { viewModel.navigateBack() },
                            onNavigateTo = { viewModel.navigateTo(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .widthIn(max = 860.dp)
                        ) {
                            ScreenRouter(
                                currentScreen = currentScreen,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        } else {
            // Standard Mobile Layout: Bottom Navigation
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    AppTopBar(
                        currentScreen = currentScreen,
                        currentProfile = currentProfile,
                        onNavigateBack = { viewModel.navigateBack() },
                        onNavigateTo = { viewModel.navigateTo(it) }
                    )
                },
                bottomBar = {
                    AppBottomBar(
                        currentScreen = currentScreen,
                        onNavigateTo = { viewModel.navigateTo(it) },
                        onOpenMoreMenu = { showMoreMenu = true }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    ScreenRouter(
                        currentScreen = currentScreen,
                        viewModel = viewModel
                    )
                }
            }
        }

        if (showMoreMenu) {
            MoreMenuSheet(
                onDismiss = { showMoreMenu = false },
                onNavigateTo = { screen ->
                    viewModel.navigateTo(screen)
                }
            )
        }
    }
}

@Composable
fun ScreenRouter(
    currentScreen: Screen,
    viewModel: MainViewModel
) {
    when (val screen = currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                viewModel = viewModel,
                onNavigateTo = { viewModel.navigateTo(it) },
                onOpenStory = { storyId -> viewModel.navigateTo(Screen.StoryDetail(storyId)) }
            )
        }

        is Screen.DailyDevotional -> {
            DailyDevotionalScreen(viewModel = viewModel)
        }

        is Screen.StoriesList -> {
            StoryListScreen(
                viewModel = viewModel,
                onOpenStory = { storyId -> viewModel.navigateTo(Screen.StoryDetail(storyId)) }
            )
        }

        is Screen.StoryDetail -> {
            StoryDetailScreen(
                storyId = screen.storyId,
                viewModel = viewModel,
                onNavigateBack = { viewModel.navigateBack() }
            )
        }

        is Screen.ThirtyDayPlan -> {
            ThirtyDayPlanScreen(
                viewModel = viewModel,
                onNavigateTo = { viewModel.navigateTo(it) }
            )
        }

        is Screen.JesusTeachings -> {
            JesusTeachingsScreen(viewModel = viewModel)
        }

        is Screen.Prayers -> {
            PrayersScreen(viewModel = viewModel)
        }

        is Screen.Activities -> {
            ActivitiesScreen(viewModel = viewModel)
        }

        is Screen.Favorites -> {
            FavoritesScreen(
                viewModel = viewModel,
                onOpenStory = { storyId -> viewModel.navigateTo(Screen.StoryDetail(storyId)) }
            )
        }

        is Screen.Progress -> {
            ProgressScreen(viewModel = viewModel)
        }

        is Screen.ParentDashboard -> {
            ParentDashboardScreen(viewModel = viewModel)
        }

        is Screen.AiPersonalization -> {
            AiPersonalizedScreen(viewModel = viewModel)
        }

        is Screen.Settings -> {
            SettingsScreen(viewModel = viewModel)
        }
    }
}
