package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.database.ChildProfileEntity
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: Screen,
    currentProfile: ChildProfileEntity?,
    onNavigateBack: () -> Unit,
    onNavigateTo: (Screen) -> Unit
) {
    val showBackButton = currentScreen !is Screen.Home

    val title = when (currentScreen) {
        is Screen.Home -> "Pequenos Corações"
        is Screen.DailyDevotional -> "Devocional do Dia"
        is Screen.StoriesList -> "Histórias Bíblicas"
        is Screen.StoryDetail -> "História Bíblica"
        is Screen.ThirtyDayPlan -> "Plano de 30 Dias"
        is Screen.JesusTeachings -> "Aprender com Jesus"
        is Screen.Prayers -> "Orações Infantis"
        is Screen.Activities -> "Actividades & Quiz"
        is Screen.Favorites -> "Favoritos"
        is Screen.Progress -> "Progresso & Medalhas"
        is Screen.ParentDashboard -> "Área dos Pais"
        is Screen.AiPersonalization -> "Devocional com IA"
        is Screen.Settings -> "Definições"
    }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentScreen is Screen.Home) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✝️", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            if (currentProfile != null) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onNavigateTo(Screen.ParentDashboard) }
                        .testTag("child_profile_chip")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(text = currentProfile.avatarEmoji, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = currentProfile.name,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = { onNavigateTo(Screen.Favorites) },
                modifier = Modifier.testTag("nav_favorites")
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favoritos",
                    tint = Color(0xFFEF4444)
                )
            }

            IconButton(
                onClick = { onNavigateTo(Screen.ParentDashboard) },
                modifier = Modifier.testTag("nav_parent_dashboard")
            ) {
                Icon(
                    imageVector = Icons.Outlined.SupervisorAccount,
                    contentDescription = "Área dos Pais",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
