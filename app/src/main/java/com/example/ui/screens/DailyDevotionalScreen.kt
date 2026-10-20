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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
fun DailyDevotionalScreen(
    viewModel: MainViewModel
) {
    val defaultDay = remember { viewModel.repository.getTodayDevotionalDay().dayNumber }
    var selectedDayNumber by remember { mutableIntStateOf(defaultDay) }

    DisposableEffect(selectedDayNumber) {
        onDispose {
            viewModel.stopSpeaking()
        }
    }

    val devotional = remember(selectedDayNumber) {
        viewModel.repository.getPlanDay(selectedDayNumber)
    }

    val planProgress by viewModel.planProgress.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()
    val fontScale by viewModel.fontSizeScale.collectAsState()

    val isCompleted = planProgress.find { it.dayNumber == selectedDayNumber }?.isCompleted ?: false

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
        // Day selector chips (1..30)
        item {
            Column {
                Text(
                    text = "Escolher Dia da Jornada:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((1..30).toList()) { day ->
                        val dayCompleted = planProgress.find { it.dayNumber == day }?.isCompleted ?: false
                        FilterChip(
                            selected = day == selectedDayNumber,
                            onClick = { selectedDayNumber = day },
                            label = {
                                Text(
                                    text = if (dayCompleted) "Dia $day ✓" else "Dia $day",
                                    fontWeight = if (day == selectedDayNumber) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PastelSkyBluePrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("day_chip_$day")
                        )
                    }
                }
            }
        }

        // Header Card with Title and Narration
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
                                text = "Dia ${devotional.dayNumber} • ${devotional.theme}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = PastelSkyBluePrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        // Audio Narration Button
                        Button(
                            onClick = {
                                if (isSpeaking) {
                                    viewModel.stopSpeaking()
                                } else {
                                    val fullNarration = "${devotional.title}. ${devotional.verseText}. ${devotional.message}. Oração: ${devotional.prayer}"
                                    viewModel.speakText(fullNarration)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSpeaking) Color(0xFFEF4444) else PastelSkyBluePrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_listen_devotional")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                                contentDescription = if (isSpeaking) "Parar" else "Ouvir",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isSpeaking) "Parar" else "Ouvir")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = devotional.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = (24 * fontScale).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Scripture Verse Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GoldenYellowContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📖", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Versículo para Guardar no Coração",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = GentleGoldenYellow
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "\"${devotional.verseText}\"",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = (18 * fontScale).sp,
                            lineHeight = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "— ${devotional.scriptureRef}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = GentleGoldenYellow
                    )
                }
            }
        }

        // Devotional Message Text Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💭", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reflexão para Pequenos Corações",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = devotional.message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (16 * fontScale).sp,
                            lineHeight = (26 * fontScale).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(text = "💡", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = devotional.practicalTip,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Prayer Card
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
                            text = "Vamos Orar Juntos",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SoftLavenderTertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = devotional.prayer,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = (17 * fontScale).sp,
                            lineHeight = (26 * fontScale).sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Kindness Challenge Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SageGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🤝", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Desafio de Bondade de Hoje",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SageGreenSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = devotional.kindnessChallenge,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Complete Day Button
        item {
            Button(
                onClick = { viewModel.togglePlanDay(selectedDayNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_complete_devotional_day"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) SageGreenSecondary else PastelSkyBluePrimary
                )
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Check,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCompleted) "Dia Concluído! Parabéns! 🌟" else "Marcar Devocional como Concluído",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
}

val GoldenYellowContainer = Color(0xFFFFF7E6)
