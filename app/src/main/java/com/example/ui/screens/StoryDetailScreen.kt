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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.MainViewModel

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.DisposableEffect

@Composable
fun StoryDetailScreen(
    storyId: String,
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopSpeaking()
        }
    }

    val story = remember(storyId) { viewModel.repository.getStoryById(storyId) }
    if (story == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("História não encontrada.")
        }
        return
    }

    val storyProgress by viewModel.storyProgress.collectAsState()
    val progress = storyProgress.find { it.storyId == story.id }
    val isFavorite = progress?.isFavorite ?: false
    val isRead = progress?.isRead ?: false
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()
    val fontScale by viewModel.fontSizeScale.collectAsState()

    var readingDoneCelebrated by remember { mutableStateOf(isRead) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = 820.dp)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Story Header
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PastelSkyBlueContainer
                        ) {
                            Text(
                                text = "${story.testament} • ${story.ageBracket}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = PastelSkyBluePrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { viewModel.toggleFavorite(story.id) }) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favorito",
                                    tint = if (isFavorite) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // TTS Narrator
                            Button(
                                onClick = {
                                    if (isSpeaking) {
                                        viewModel.stopSpeaking()
                                    } else {
                                        val fullStoryText = "${story.title}. " +
                                                story.storyParagraphs.joinToString(" ") +
                                                ". Versículo para o coração: ${story.memoryVerse}. " +
                                                "Oração: ${story.childPrayer}"
                                        viewModel.speakText(fullStoryText)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSpeaking) Color(0xFFEF4444) else PastelSkyBluePrimary
                                ),
                                modifier = Modifier.testTag("btn_story_tts")
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isSpeaking) "Parar" else "Ouvir")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(PastelSkyBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = story.iconEmoji, fontSize = 30.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = story.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = (22 * fontScale).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${story.scriptureReference} • Cerca de ${story.readingMinutes} min de leitura",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Font Size Adjuster Controls
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatSize,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Letra:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = fontScale == 0.9f,
                    onClick = { viewModel.setFontSizeScale(0.9f) },
                    label = { Text("A-") }
                )
                Spacer(modifier = Modifier.width(4.dp))
                FilterChip(
                    selected = fontScale == 1.0f,
                    onClick = { viewModel.setFontSizeScale(1.0f) },
                    label = { Text("A") }
                )
                Spacer(modifier = Modifier.width(4.dp))
                FilterChip(
                    selected = fontScale == 1.2f,
                    onClick = { viewModel.setFontSizeScale(1.2f) },
                    label = { Text("A+") }
                )
            }
        }

        // Story Paragraphs
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    story.storyParagraphs.forEachIndexed { index, paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = (17 * fontScale).sp,
                                lineHeight = (28 * fontScale).sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }

        // Memory Verse Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GoldenYellowContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Versículo para Guardar no Coração",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = GentleGoldenYellow
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${story.memoryVerse}\"",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = (17 * fontScale).sp,
                            lineHeight = 25.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "— ${story.memoryVerseRef}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GentleGoldenYellow
                    )
                }
            }
        }

        // O Que Aprendemos Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSkyBlueContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "O que Aprendemos com Esta História",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = PastelSkyBluePrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = story.whatWeLearn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (15 * fontScale).sp,
                            lineHeight = 23.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Para Conversar em Família
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💬", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Para Conversar em Família",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    story.familyDiscussion.forEach { question ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(
                                text = question,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (15 * fontScale).sp,
                                    lineHeight = 22.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Oraçãozinha Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🙏", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Oraçãozinha",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = SoftLavenderTertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = story.childPrayer,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            fontSize = (16 * fontScale).sp,
                            lineHeight = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Desafio de Bondade
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SageGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌱", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Desafio de Bondade",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = SageGreenSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = story.kindnessAction,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (15 * fontScale).sp,
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Mark as Read / Complete Reading Button
        item {
            Button(
                onClick = {
                    viewModel.markStoryRead(story.id)
                    readingDoneCelebrated = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_complete_story_reading"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (readingDoneCelebrated) SageGreenSecondary else PastelSkyBluePrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (readingDoneCelebrated) "História Concluída com Sucesso! 🌟" else "Concluir Leitura desta História",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
}
