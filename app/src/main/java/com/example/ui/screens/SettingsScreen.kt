package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val fontScale by viewModel.fontSizeScale.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Font Size Settings Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Tamanho da Letra (Acessibilidade)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ajusta o tamanho das histórias para facilitar a leitura das crianças.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = fontScale == 0.9f,
                            onClick = { viewModel.setFontSizeScale(0.9f) },
                            label = { Text("Pequeno") }
                        )
                        FilterChip(
                            selected = fontScale == 1.0f,
                            onClick = { viewModel.setFontSizeScale(1.0f) },
                            label = { Text("Normal") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PastelSkyBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = fontScale == 1.15f,
                            onClick = { viewModel.setFontSizeScale(1.15f) },
                            label = { Text("Grande") }
                        )
                        FilterChip(
                            selected = fontScale == 1.3f,
                            onClick = { viewModel.setFontSizeScale(1.3f) },
                            label = { Text("Extra") }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "Exemplo: 'Jesus ama os pequeninos e abençoa a nossa família.'",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = (16 * fontScale).sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Voice Narration Speed
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Velocidade da Narração em Voz Alta",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Configuração do leitor de áudio para crianças.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.ttsManager.setSpeed(0.8f) },
                            label = { Text("Calmo (0.8x)") }
                        )
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.ttsManager.setSpeed(0.95f) },
                            label = { Text("Suave (Normal)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PastelSkyBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.ttsManager.setSpeed(1.15f) },
                            label = { Text("Rápido (1.2x)") }
                        )
                    }
                }
            }
        }

        // Language & Cultural Localization
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Idioma e Cultura",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PastelSkyBlueContainer
                    ) {
                        Text(
                            text = "🇲🇿 Português (Moçambique) — Ativo\nLinguagem natural, calorosa e respeitosa, adequada para famílias e escolas em Moçambique e no mundo lusófono.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Principles & Statement of Faith
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SageGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Propósito e Princípios Bíblicos",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SageGreenSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Jesus ama as crianças incondicionalmente.\n" +
                                "• Sem medo, ameaças ou manipulação emocional.\n" +
                                "• Distinção clara entre o relato bíblico e as explicações infantis.\n" +
                                "• Foco no amor a Deus, amor ao próximo, perdão, paciência e gratidão.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // App Information
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Pequenos Corações, Grande Fé",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Versão 1.0 • Desenvolvido para famílias cristãs, pais e educadores.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
