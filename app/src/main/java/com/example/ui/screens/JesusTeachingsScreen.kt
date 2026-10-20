package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JesusTeaching
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun JesusTeachingsScreen(viewModel: MainViewModel) {
    val teachings = viewModel.repository.jesusTeachings
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()
    val fontScale by viewModel.fontSizeScale.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🕊️", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Aprender com Jesus",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SoftLavenderTertiary
                        )
                        Text(
                            text = "Palavras de amor, perdão e sabedoria que transformam o nosso dia a dia.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        items(teachings, key = { it.id }) { teaching ->
            JesusTeachingCard(
                teaching = teaching,
                isSpeaking = isSpeaking,
                fontScale = fontScale,
                onListen = {
                    val full = "${teaching.title}. ${teaching.teachingSummary}. Exemplo prático: ${teaching.practicalExample}"
                    viewModel.speakText(full)
                },
                onStop = { viewModel.stopSpeaking() }
            )
        }
    }
}

@Composable
fun JesusTeachingCard(
    teaching: JesusTeaching,
    isSpeaking: Boolean,
    fontScale: Float,
    onListen: () -> Unit,
    onStop: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("teaching_card_${teaching.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PastelSkyBlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = teaching.emoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = teaching.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = teaching.scriptureRef,
                        style = MaterialTheme.typography.labelSmall,
                        color = PastelSkyBluePrimary
                    )
                }

                IconButton(
                    onClick = { if (isSpeaking) onStop() else onListen() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = "Ouvir",
                        tint = PastelSkyBluePrimary
                    )
                }

                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "Ver mais"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = teaching.subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = teaching.teachingSummary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (15 * fontScale).sp,
                            lineHeight = 23.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PastelSkyBlueContainer
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Como praticar hoje:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = PastelSkyBluePrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = teaching.practicalExample,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SageGreenContainer
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Desafio Prático:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = SageGreenSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = teaching.actionChallenge,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
