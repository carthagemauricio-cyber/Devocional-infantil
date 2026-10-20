package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BibleStory
import com.example.data.model.DevotionalDay
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateTo: (Screen) -> Unit,
    onOpenStory: (String) -> Unit
) {
    val todayDevotional = remember { viewModel.repository.getTodayDevotionalDay() }
    val storyProgress by viewModel.storyProgress.collectAsState()
    val planProgress by viewModel.planProgress.collectAsState()
    val currentProfile by viewModel.currentProfile.collectAsState()
    val favorites by viewModel.favoriteStories.collectAsState()

    val completedPlanDaysCount = planProgress.count { it.isCompleted }
    val planPercentage = (completedPlanDaysCount / 30f).coerceIn(0f, 1f)

    // Find last read story
    val lastReadEntity = storyProgress.filter { it.lastReadTimestamp > 0 }
        .maxByOrNull { it.lastReadTimestamp }
    val lastReadStory = lastReadEntity?.let { viewModel.repository.getStoryById(it.storyId) }

    var kindnessCompletedToday by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Banner with welcoming Jesus illustration
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_jesus_children_1792488824021),
                    contentDescription = "Jesus com as crianças",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                // Child greeting chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = currentProfile?.avatarEmoji ?: "🌟", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Olá, ${currentProfile?.name ?: "Pequeno Coração"}!",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Welcome Headline & Subtitle
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Pequenos corações, grandes descobertas com Jesus.",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 30.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descobre a Bíblia em família, aprende com Jesus e transforma cada dia numa oportunidade de crescer em amor, fé e bondade.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { onNavigateTo(Screen.DailyDevotional) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("start_daily_devotional_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PastelSkyBluePrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Começar o Devocional de Hoje",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }

        // Devocional do Dia Card
        item {
            Spacer(modifier = Modifier.height(20.dp))
            DevotionalDayCard(
                devotional = todayDevotional,
                onStart = { onNavigateTo(Screen.DailyDevotional) },
                onListen = { viewModel.speakText("${todayDevotional.title}. ${todayDevotional.message}") }
            )
        }

        // Continuar a Leitura (if available)
        if (lastReadStory != null) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Continuar a Leitura",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenStory(lastReadStory.id) }
                            .testTag("continue_reading_card")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text(text = lastReadStory.iconEmoji, fontSize = 32.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = lastReadStory.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = lastReadStory.scriptureReference,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Continuar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // 30-Day Plan Completion Card
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSkyBlueContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { onNavigateTo(Screen.ThirtyDayPlan) }
                    .testTag("plan_progress_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📅", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Plano de 30 Dias",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            text = "$completedPlanDaysCount / 30 Dias",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { planPercentage },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PastelSkyBluePrimary,
                        trackColor = Color.White.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Conhecendo o Amor de Deus dia a dia em família.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Desafio de Bondade de Hoje Card
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SageGreenContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .testTag("kindness_challenge_home_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌱", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Desafio de Bondade de Hoje",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SageGreenSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = todayDevotional.kindnessChallenge,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            kindnessCompletedToday = !kindnessCompletedToday
                            if (kindnessCompletedToday) {
                                viewModel.completeKindnessChallenge(todayDevotional.kindnessChallenge)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (kindnessCompletedToday) SageGreenSecondary else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_kindness_done")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = if (kindnessCompletedToday) Color.White else SageGreenSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (kindnessCompletedToday) "Cumprido com Amor! ❤️" else "Eu Fiz o Desafio!",
                            color = if (kindnessCompletedToday) Color.White else SageGreenSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Histórias em Destaque
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Histórias em Destaque",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Ver todas (${viewModel.repository.allStories.size})",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateTo(Screen.StoriesList) }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(viewModel.repository.allStories.take(8)) { story ->
                    FeaturedStoryCard(
                        story = story,
                        onClick = { onOpenStory(story.id) }
                    )
                }
            }
        }

        // Aprender com Jesus Spotlight Card
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { onNavigateTo(Screen.JesusTeachings) }
                    .testTag("card_jesus_teachings_spotlight")
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🕊️", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Aprender com Jesus",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SoftLavenderTertiary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Descobre o Pai Nosso, as Bem-aventuranças e o grande mandamento do Amor.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = SoftLavenderTertiary
                    )
                }
            }
        }

        // Vamos Orar Juntos Card
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { onNavigateTo(Screen.Prayers) }
                    .testTag("card_prayers_shortcut")
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(GentleGoldenYellow.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🙏", fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Vamos Orar Juntos",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Orações de bom dia, hora de dormir, família e o Diário de Oração.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = GentleGoldenYellow
                    )
                }
            }
        }

        // Devocional Personalizado com IA Card
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { onNavigateTo(Screen.AiPersonalization) }
                    .testTag("card_ai_personalization_shortcut")
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✨", fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Criar Devocional com IA",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cria uma oração ou história com o nome do teu filho para situações do dia a dia.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun DevotionalDayCard(
    devotional: DevotionalDay,
    onStart: () -> Unit,
    onListen: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .testTag("devotional_day_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GentleGoldenYellow.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "⭐ Devocional do Dia • Dia ${devotional.dayNumber}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = GentleGoldenYellow,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                IconButton(
                    onClick = onListen,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Ouvir Devocional",
                        tint = PastelSkyBluePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = devotional.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "\"${devotional.verseText}\"",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "— ${devotional.scriptureRef}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = devotional.message,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = onStart,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ler Devocional Completo e Orar", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun FeaturedStoryCard(
    story: BibleStory,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick)
            .testTag("featured_story_${story.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = story.iconEmoji, fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = story.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = story.scriptureReference,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = story.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
