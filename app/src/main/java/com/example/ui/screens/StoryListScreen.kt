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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BibleStory
import com.example.data.model.StoryCategory
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBlueContainer
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun StoryListScreen(
    viewModel: MainViewModel,
    onOpenStory: (String) -> Unit
) {
    val stories by viewModel.filteredStories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedAge by viewModel.selectedAgeBracket.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val storyProgress by viewModel.storyProgress.collectAsState()

    val categories = StoryCategory.entries
    val ageBrackets = listOf("Todas", "3-5 anos", "6-8 anos", "Todas as idades")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = 860.dp)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
        // Search Input
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("story_search_input"),
                placeholder = { Text("Pesquisar histórias, personagens, versículos...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Pesquisar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Limpar")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = PastelSkyBluePrimary
                ),
                singleLine = true
            )
        }

        // Category Chips
        item {
            Column {
                Text(
                    text = "Categorias Bíblicas:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        FilterChip(
                            selected = category == selectedCategory,
                            onClick = { viewModel.setCategory(category) },
                            label = { Text("${category.emoji} ${category.displayName}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PastelSkyBluePrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("category_chip_${category.name}")
                        )
                    }
                }
            }
        }

        // Age Filter Chips
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Idade: ",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ageBrackets) { age ->
                        FilterChip(
                            selected = age == selectedAge,
                            onClick = { viewModel.setAgeBracket(age) },
                            label = { Text(age) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SageGreenSecondary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Story Count Indicator
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stories.size} histórias bíblicas encontradas",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Story Cards List
        items(stories, key = { it.id }) { story ->
            val progress = storyProgress.find { it.storyId == story.id }
            val isFav = progress?.isFavorite ?: false
            val isRead = progress?.isRead ?: false

            StoryListItemCard(
                story = story,
                isFavorite = isFav,
                isRead = isRead,
                onToggleFavorite = { viewModel.toggleFavorite(story.id) },
                onClick = { onOpenStory(story.id) }
            )
        }
    }
}

@Composable
fun StoryListItemCard(
    story: BibleStory,
    isFavorite: Boolean,
    isRead: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("story_card_${story.id}")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PastelSkyBlueContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = story.iconEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = story.testament,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    if (isRead) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SageGreenContainer
                        ) {
                            Text(
                                text = "Lida ✓",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = SageGreenSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = story.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${story.scriptureReference} • ${story.readingMinutes} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = story.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.testTag("fav_btn_${story.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remover Favorito" else "Adicionar Favorito",
                    tint = if (isFavorite) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
