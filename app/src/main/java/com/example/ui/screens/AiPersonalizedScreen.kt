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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SoftLavenderContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPersonalizedScreen(viewModel: MainViewModel) {
    val currentProfile by viewModel.currentProfile.collectAsState()
    val aiLoading by viewModel.aiLoading.collectAsState()
    val aiResult by viewModel.aiGeneratedResult.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()
    val fontScale by viewModel.fontSizeScale.collectAsState()

    var childName by remember(currentProfile) { mutableStateOf(currentProfile?.name ?: "Samuel") }
    var childAge by remember(currentProfile) { mutableIntStateOf(currentProfile?.age ?: 7) }
    var selectedTypeIndex by remember { mutableIntStateOf(0) } // 0: Oração, 1: História
    var topicText by remember { mutableStateOf("") }

    val suggestions = listOf(
        "Primeiro dia de aulas na escola",
        "Medo do escuro e de trovoadas",
        "Briguei com o irmão e quero fazer as pazes",
        "Aprender a partilhar com os amigos",
        "Saudades da vovó e da família",
        "Agradecer por um novo amigo"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Card
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
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = SoftLavenderTertiary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Devocional Personalizado com IA",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SoftLavenderTertiary
                        )
                        Text(
                            text = "Cria uma oração carinhosa ou uma história com o nome do teu filho para as situações de hoje.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Form Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "1. Informações da Criança",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = childName,
                        onValueChange = { childName = it },
                        label = { Text("Nome da Criança") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ai_child_name"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Idade: $childAge anos",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items((3..12).toList()) { age ->
                            FilterChip(
                                selected = childAge == age,
                                onClick = { childAge = age },
                                label = { Text("$age anos") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PastelSkyBluePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "2. O que queres criar?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PrimaryTabRow(selectedTabIndex = selectedTypeIndex) {
                        Tab(
                            selected = selectedTypeIndex == 0,
                            onClick = { selectedTypeIndex = 0 },
                            text = { Text("Oração Doce 🙏") }
                        )
                        Tab(
                            selected = selectedTypeIndex == 1,
                            onClick = { selectedTypeIndex = 1 },
                            text = { Text("História Bíblica 📖") }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "3. O que a criança está a viver hoje?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(suggestions) { suggestion ->
                            FilterChip(
                                selected = topicText == suggestion,
                                onClick = { topicText = suggestion },
                                label = { Text(suggestion) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoftLavenderTertiary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = topicText,
                        onValueChange = { topicText = it },
                        label = { Text("Ou escreve a tua situação...") },
                        placeholder = { Text("Ex: Fiquei triste na escola, perdi um brinquedo...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ai_topic"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            viewModel.generateAiDevotional(
                                childName = childName,
                                childAge = childAge,
                                topic = topicText,
                                isPrayer = selectedTypeIndex == 0
                            )
                        },
                        enabled = !aiLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_generate_ai"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftLavenderTertiary)
                    ) {
                        if (aiLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("A criar com amor e fé...")
                        } else {
                            Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedTypeIndex == 0) "Gerar Oração com Jesus ✨" else "Gerar História com Jesus ✨",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // AI Result Card
        if (aiResult != null) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_ai_result")
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SoftLavenderContainer
                            ) {
                                Text(
                                    text = "Feito para $childName com amor",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = SoftLavenderTertiary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }

                            Row {
                                Button(
                                    onClick = {
                                        if (isSpeaking) {
                                            viewModel.stopSpeaking()
                                        } else {
                                            viewModel.speakText(aiResult ?: "")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSpeaking) Color(0xFFEF4444) else PastelSkyBluePrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isSpeaking) "Parar" else "Ouvir")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = aiResult ?: "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = (16 * fontScale).sp,
                                lineHeight = (26 * fontScale).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
