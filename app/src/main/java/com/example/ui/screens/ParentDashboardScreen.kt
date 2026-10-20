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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.database.ChildProfileEntity
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ParentDashboardScreen(viewModel: MainViewModel) {
    val profiles by viewModel.childProfiles.collectAsState()
    val currentProfile by viewModel.currentProfile.collectAsState()

    var showAddChildDialog by remember { mutableStateOf(false) }
    var newChildName by remember { mutableStateOf("") }
    var newChildAge by remember { mutableIntStateOf(6) }
    var newChildAvatar by remember { mutableStateOf("🌟") }
    var showResetDialog by remember { mutableStateOf(false) }

    val emojis = listOf("🌟", "🦁", "🌸", "🕊️", "🐑", "⛵", "👑", "🌿")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Parent Header
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSkyBlueContainer),
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
                            imageVector = Icons.Filled.SupervisorAccount,
                            contentDescription = null,
                            tint = PastelSkyBluePrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Área dos Pais & Educadores",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Apoio pedagógico e espiritual para orientar os teus filhos na fé com amor.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PastelSkyBluePrimary
                        )
                    }
                }
            }
        }

        // Child Profile Switcher
        item {
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
                        Text(
                            text = "Perfis das Crianças",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        TextButton(onClick = { showAddChildDialog = true }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Adicionar Filho")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(profiles) { profile ->
                            val isSelected = profile.id == currentProfile?.id
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) PastelSkyBlueContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { viewModel.selectProfile(profile.id) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = profile.avatarEmoji, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = profile.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (isSelected) PastelSkyBluePrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${profile.age} anos",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = PastelSkyBluePrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Parent Guides
        item {
            Text(
                text = "Guias Práticos para a Família",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            ParentGuideCard(
                title = "Como Fazer Devocionais sem Pressão",
                emoji = "🌱",
                content = "O momento devocional deve ser o momento mais afetuoso e acolhedor do dia, nunca uma prova ou aula pesada. Dez a quinze minutos de conversa leve, leitura da história e uma oração sincera de mãos dadas valem muito mais do que longos discursos. Deixa a criança perguntar livremente e expressar os seus sentimentos sem medo de errar."
            )
        }

        item {
            ParentGuideCard(
                title = "Respondendo a Perguntas Difíceis sobre Deus",
                emoji = "❓",
                content = "Se o teu filho perguntar 'Onde está Deus?' ou 'Porque acontecem coisas tristes?', responde com naturalidade e acolhimento. Diz com sinceridade: 'Deus é Espírito, Ele está em todo o lado e cuida do nosso coração'. Explica que Jesus compreende a tristeza e que nós somos chamados a levar consolo e amor aos que sofrem."
            )
        }

        item {
            ParentGuideCard(
                title = "Criando o Hábito da Oração",
                emoji = "🙏",
                content = "A melhor maneira de ensinar a orar é orando junto nas pequenas coisas: agradecendo pela comida quentinha à mesa, pedindo bênção ao sair para a escola e abençoando o sono na cama. O exemplo diário dos pais ensina mais do que mil regras."
            )
        }

        item {
            ParentGuideCard(
                title = "Perguntas de Conversa para a Mesa de Jantar",
                emoji = "🍲",
                content = "Experimenta perguntar durante a refeição em família:\n1. 'Qual foi a melhor coisa que Deus te deu hoje?'\n2. 'Quem precisou de ajuda na escola e o que pudemos fazer?'\n3. 'Houve algum momento em que tiveste de ter paciência hoje?'\nEstas perguntas criam pontes profundas de comunicação entre pais e filhos."
            )
        }

        // Data Management / Reset
        item {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
            ) {
                Text("Repor Progresso de Leitura")
            }
        }
    }

    // Add Child Dialog
    if (showAddChildDialog) {
        AlertDialog(
            onDismissRequest = { showAddChildDialog = false },
            title = { Text("Adicionar Perfil de Filho") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newChildName,
                        onValueChange = { newChildName = it },
                        label = { Text("Nome da Criança") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Idade: $newChildAge anos", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (3..12).filter { it in listOf(4, 6, 8, 10, 12) }.forEach { age ->
                            FilterChip(
                                selected = newChildAge == age,
                                onClick = { newChildAge = age },
                                label = { Text("$age") }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Escolhe um Avatar:", style = MaterialTheme.typography.bodyMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(emojis) { emoji ->
                            Surface(
                                shape = CircleShape,
                                color = if (newChildAvatar == emoji) PastelSkyBlueContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { newChildAvatar = emoji }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newChildName.isNotBlank()) {
                            viewModel.createChildProfile(newChildName, newChildAge, newChildAvatar)
                            newChildName = ""
                            showAddChildDialog = false
                        }
                    }
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddChildDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Repor Progresso?") },
            text = { Text("Isto irá reiniciar as leituras concluídas e o plano de 30 dias para poderes começar uma nova jornada.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllProgress()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Sim, Repor")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ParentGuideCard(
    title: String,
    emoji: String,
    content: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
