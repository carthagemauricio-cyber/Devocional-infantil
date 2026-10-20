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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.database.PrayerJournalEntity
import com.example.data.model.PrayerItem
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun PrayersScreen(viewModel: MainViewModel) {
    val allPrayers = viewModel.repository.prayers
    val prayerJournal by viewModel.prayerJournal.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()
    val fontScale by viewModel.fontSizeScale.collectAsState()

    var selectedCategory by remember { mutableStateOf("Todas") }
    var newPrayerText by remember { mutableStateOf("") }
    var showAddJournal by remember { mutableStateOf(false) }

    val categories = listOf("Todas", "Manhã", "Hora de Dormir", "Quando Sinto Medo", "Gratidão", "Pela Família", "À Mesa", "Perdão", "Moçambique & Mundo")

    val filteredPrayers = if (selectedCategory == "Todas") {
        allPrayers
    } else {
        allPrayers.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner
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
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🙏", fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Tesouro de Orações",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SoftLavenderTertiary
                        )
                        Text(
                            text = "Aprende a falar com Deus como um Pai amoroso em qualquer momento.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftLavenderTertiary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Prayer Cards
        items(filteredPrayers, key = { it.id }) { prayer ->
            PrayerCard(
                prayer = prayer,
                isSpeaking = isSpeaking,
                fontScale = fontScale,
                onListen = {
                    val full = "${prayer.title}. ${prayer.prayerText}. ${prayer.verseReference}"
                    viewModel.speakText(full)
                },
                onStop = { viewModel.stopSpeaking() }
            )
        }

        // Family Prayer Journal Section Header
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSkyBlueContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📔", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Meu Diário de Orações",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = PastelSkyBluePrimary
                                )
                                Text(
                                    text = "Pedidos e agradecimentos em família",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Button(
                            onClick = { showAddJournal = !showAddJournal },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PastelSkyBluePrimary)
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pedir")
                        }
                    }

                    if (showAddJournal) {
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = newPrayerText,
                            onValueChange = { newPrayerText = it },
                            label = { Text("Pelo que queres orar hoje?") },
                            placeholder = { Text("Ex: Pela saúde da vovó, pelo exame da escola...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_new_prayer"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (newPrayerText.isNotBlank()) {
                                    viewModel.addPrayerRequest(newPrayerText)
                                    newPrayerText = ""
                                    showAddJournal = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PastelSkyBluePrimary)
                        ) {
                            Text("Guardar no Diário de Oração 🙏")
                        }
                    }
                }
            }
        }

        // Family Prayer Journal Items
        if (prayerJournal.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ainda não tens orações registadas no diário. Clica em 'Pedir' para adicionar!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(prayerJournal, key = { it.id }) { journalItem ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (journalItem.isAnswered) SageGreenContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = journalItem.isAnswered,
                            onCheckedChange = { viewModel.togglePrayerAnswered(journalItem) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = journalItem.title,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (journalItem.isAnswered) {
                                Text(
                                    text = "Deus respondeu a esta oração! Glória a Deus! 🙌",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = SageGreenSecondary
                                )
                            }
                        }
                        IconButton(onClick = { viewModel.deletePrayerRequest(journalItem.id) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerCard(
    prayer: PrayerItem,
    isSpeaking: Boolean,
    fontScale: Float,
    onListen: () -> Unit,
    onStop: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SoftLavenderContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = prayer.emoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = prayer.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = prayer.category,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { if (isSpeaking) onStop() else onListen() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = "Ouvir",
                        tint = SoftLavenderTertiary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "\"${prayer.prayerText}\"",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontStyle = FontStyle.Italic,
                    fontSize = (16 * fontScale).sp,
                    lineHeight = 24.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = prayer.verseReference,
                style = MaterialTheme.typography.labelSmall,
                color = SoftLavenderTertiary
            )
        }
    }
}
