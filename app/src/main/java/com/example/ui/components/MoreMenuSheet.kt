package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GentleGoldenYellow
import com.example.ui.theme.PastelSkyBluePrimary
import com.example.ui.theme.SageGreenSecondary
import com.example.ui.theme.SoftLavenderTertiary
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreMenuSheet(
    onDismiss: () -> Unit,
    onNavigateTo: (Screen) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Todas as Seções",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            MoreMenuItem(
                icon = Icons.Filled.SelfImprovement,
                iconBg = SoftLavenderTertiary.copy(alpha = 0.15f),
                iconTint = SoftLavenderTertiary,
                title = "Aprender com Jesus",
                subtitle = "Pai Nosso, Bem-aventuranças, Perdão e Amor",
                onClick = { onDismiss(); onNavigateTo(Screen.JesusTeachings) }
            )

            MoreMenuItem(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                iconBg = PastelSkyBluePrimary.copy(alpha = 0.15f),
                iconTint = PastelSkyBluePrimary,
                title = "Orações Infantis & Diário",
                subtitle = "Manhã, dormir, família, refeições e gratidão",
                onClick = { onDismiss(); onNavigateTo(Screen.Prayers) }
            )

            MoreMenuItem(
                icon = Icons.Filled.Psychology,
                iconBg = GentleGoldenYellow.copy(alpha = 0.15f),
                iconTint = GentleGoldenYellow,
                title = "Actividades & Quiz Bíblico",
                subtitle = "Perguntas interativas e Desafios de Bondade",
                onClick = { onDismiss(); onNavigateTo(Screen.Activities) }
            )

            MoreMenuItem(
                icon = Icons.Filled.EmojiEvents,
                iconBg = SageGreenSecondary.copy(alpha = 0.15f),
                iconTint = SageGreenSecondary,
                title = "Progresso & Medalhas",
                subtitle = "Dias seguidos, histórias lidas e conquistas",
                onClick = { onDismiss(); onNavigateTo(Screen.Progress) }
            )

            MoreMenuItem(
                icon = Icons.Filled.Favorite,
                iconBg = Color(0xFFFFEBEE),
                iconTint = Color(0xFFEF4444),
                title = "Histórias Favoritas",
                subtitle = "Acede rapidamente às tuas histórias guardadas",
                onClick = { onDismiss(); onNavigateTo(Screen.Favorites) }
            )

            MoreMenuItem(
                icon = Icons.Filled.AutoAwesome,
                iconBg = SoftLavenderTertiary.copy(alpha = 0.15f),
                iconTint = SoftLavenderTertiary,
                title = "Devocional Personalizado com IA",
                subtitle = "Gera uma oração ou história com o nome do teu filho",
                onClick = { onDismiss(); onNavigateTo(Screen.AiPersonalization) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            MoreMenuItem(
                icon = Icons.Filled.SupervisorAccount,
                iconBg = PastelSkyBluePrimary.copy(alpha = 0.15f),
                iconTint = PastelSkyBluePrimary,
                title = "Área dos Pais & Perfis",
                subtitle = "Perfis dos filhos, guias familiares e dicas",
                onClick = { onDismiss(); onNavigateTo(Screen.ParentDashboard) }
            )

            MoreMenuItem(
                icon = Icons.Filled.Settings,
                iconBg = MaterialTheme.colorScheme.surfaceVariant,
                iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                title = "Definições",
                subtitle = "Tamanho de letra, narração e idioma",
                onClick = { onDismiss(); onNavigateTo(Screen.Settings) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MoreMenuItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp)
            .testTag("menu_item_${title.take(10)}")
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
