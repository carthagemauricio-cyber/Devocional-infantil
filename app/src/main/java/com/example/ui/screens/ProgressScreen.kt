package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeDef
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ProgressScreen(viewModel: MainViewModel) {
    val currentProfile by viewModel.currentProfile.collectAsState()
    val storyProgress by viewModel.storyProgress.collectAsState()
    val planProgress by viewModel.planProgress.collectAsState()
    val kindnessLogs by viewModel.kindnessLogs.collectAsState()
    val quizScore by viewModel.quizScore.collectAsState()

    val storiesReadCount = storyProgress.count { it.isRead }
    val devotionalsCompletedCount = planProgress.count { it.isCompleted }
    val kindnessCount = kindnessLogs.size

    val badges = viewModel.repository.badgesDef

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Child Profile Header & Streak
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSkyBlueContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentProfile?.avatarEmoji ?: "🌟", fontSize = 34.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentProfile?.name ?: "Pequeno Discípulo",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${currentProfile?.age ?: 7} anos • Amigo de Jesus",
                        style = MaterialTheme.typography.bodySmall,
                        color = PastelSkyBluePrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🔥", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sequência de Leitura: ${currentProfile?.streakDays ?: 1} dias seguidos",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = GentleGoldenYellow
                            )
                        }
                    }
                }
            }
        }

        // Stats Overview Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    emoji = "📖",
                    count = storiesReadCount,
                    label = "Histórias Lidas",
                    containerColor = SoftLavenderContainer,
                    accentColor = SoftLavenderTertiary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    emoji = "📅",
                    count = devotionalsCompletedCount,
                    label = "Devocionais",
                    containerColor = PastelSkyBlueContainer,
                    accentColor = PastelSkyBluePrimary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    emoji = "❤️",
                    count = kindnessCount,
                    label = "Actos de Bondade",
                    containerColor = SageGreenContainer,
                    accentColor = SageGreenSecondary
                )
            }
        }

        // Badges Section Header
        item {
            Text(
                text = "Medalhas e Conquistas Espirituais",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Badges List
        items(badges, key = { it.id }) { badge ->
            val isUnlocked = when (badge.type) {
                "stories" -> storiesReadCount >= badge.requiredCount
                "devotionals" -> devotionalsCompletedCount >= badge.requiredCount
                "kindness" -> kindnessCount >= badge.requiredCount
                "quiz" -> quizScore >= badge.requiredCount
                else -> false
            }

            BadgeCard(badge = badge, isUnlocked = isUnlocked)
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier,
    emoji: String,
    count: Int,
    label: String,
    containerColor: Color,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = accentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun BadgeCard(badge: BadgeDef, isUnlocked: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) GentleGoldenYellow.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) badge.iconEmoji else "🔒",
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isUnlocked) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SageGreenContainer
                ) {
                    Text(
                        text = "Conquistada! ⭐",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = SageGreenSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Em Progresso",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
