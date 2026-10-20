package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.SoftLavenderTertiary
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(viewModel: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val quizQuestions = viewModel.repository.quizQuestions
    val currentQuizIndex by viewModel.currentQuizIndex.collectAsState()
    val quizScore by viewModel.quizScore.collectAsState()
    val selectedOption by viewModel.selectedQuizOption.collectAsState()
    val answerSubmitted by viewModel.quizAnswerSubmitted.collectAsState()

    val kindnessLogs by viewModel.kindnessLogs.collectAsState()
    val kindnessList = viewModel.repository.kindnessChallengesList
    var currentRandomMission by remember { mutableStateOf(kindnessList.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Quiz Bíblico", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Desafios de Bondade", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            // Quiz Tab
            val question = quizQuestions.getOrNull(currentQuizIndex)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (question != null) {
                    item {
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
                                    Text(
                                        text = "Pergunta ${currentQuizIndex + 1} de ${quizQuestions.size}",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = PastelSkyBluePrimary
                                    )
                                    Text(
                                        text = "Pontuação: $quizScore ⭐",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = GentleGoldenYellow
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { (currentQuizIndex + 1).toFloat() / quizQuestions.size },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = PastelSkyBluePrimary,
                                    trackColor = Color.White
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = question.question,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 28.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(18.dp))

                                question.options.forEachIndexed { index, option ->
                                    val isSelected = selectedOption == index
                                    val isCorrect = index == question.correctIndex

                                    val bgColor = when {
                                        answerSubmitted && isCorrect -> SageGreenContainer
                                        answerSubmitted && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                                        isSelected -> PastelSkyBlueContainer
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }

                                    val textColor = when {
                                        answerSubmitted && isCorrect -> SageGreenSecondary
                                        answerSubmitted && isSelected && !isCorrect -> Color(0xFFD32F2F)
                                        isSelected -> PastelSkyBluePrimary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = bgColor,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clickable(enabled = !answerSubmitted) {
                                                viewModel.selectQuizOption(index)
                                            }
                                            .testTag("quiz_option_$index")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${('A' + index)}. ",
                                                fontWeight = FontWeight.Bold,
                                                color = textColor
                                            )
                                            Text(
                                                text = option,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                                color = textColor,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (answerSubmitted) {
                                                if (isCorrect) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Check,
                                                        contentDescription = null,
                                                        tint = SageGreenSecondary
                                                    )
                                                } else if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Close,
                                                        contentDescription = null,
                                                        tint = Color(0xFFD32F2F)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                if (answerSubmitted) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (selectedOption == question.correctIndex) SageGreenContainer else SoftLavenderContainer
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = if (selectedOption == question.correctIndex) "✨ Muito bem! Resposta Certa!" else "💡 Vamos aprender:",
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                                color = if (selectedOption == question.correctIndex) SageGreenSecondary else SoftLavenderTertiary
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = question.explanation,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                if (!answerSubmitted) {
                                    Button(
                                        onClick = { viewModel.submitQuizAnswer() },
                                        enabled = selectedOption != null,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = PastelSkyBluePrimary)
                                    ) {
                                        Text("Confirmar Resposta", fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    if (currentQuizIndex + 1 < quizQuestions.size) {
                                        Button(
                                            onClick = { viewModel.nextQuizQuestion() },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = PastelSkyBluePrimary)
                                        ) {
                                            Text("Próxima Pergunta →", fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Button(
                                            onClick = { viewModel.resetQuiz() },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = SageGreenSecondary)
                                        ) {
                                            Text("Recomeçar o Quiz 🔄", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Kindness Missions Tab
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = SageGreenContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🎲", fontSize = 26.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Missão de Bondade de Hoje",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = SageGreenSecondary
                                    )
                                    Text(
                                        text = "Pequenos gestos de amor que alegram Jesus",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White
                            ) {
                                Text(
                                    text = "\"$currentRandomMission\"",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        lineHeight = 24.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(
                                    onClick = {
                                        currentRandomMission = kindnessList.random()
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Outra Missão")
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Button(
                                    onClick = {
                                        viewModel.completeKindnessChallenge(currentRandomMission)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SageGreenSecondary)
                                ) {
                                    Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Eu Cumpri! ❤️")
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Histórico de Bondades Realizadas (${kindnessLogs.size}):",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (kindnessLogs.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Ainda não registaste nenhuma missão de bondade. Cumpre o desafio acima e clica em 'Eu Cumpri!'",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(kindnessLogs.size) { index ->
                        val log = kindnessLogs[index]
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "❤️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = log.challengeText,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Realizado com amor por ${log.childName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SageGreenSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
