package com.example.feature_heroes_list.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.COLOR_STAR
import com.example.core.database.entities.HeroEntity
import com.example.feature_heroes_list.R
import com.example.feature_heroes_list.presentation.viewmodel.HeroesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroItem(
    hero: HeroEntity,
    onClick: () -> Unit,
    viewModel: HeroesListViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
//                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Изображение героя
            AsyncImage(
                model = hero.images.urls?.firstOrNull(),
                contentDescription = hero.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.error)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о герое
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hero.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = hero.biographies.fullName ?: "Unknown",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PublisherIcon(
                    publisher = hero.biographies.publisher,
                    modifier = Modifier.size(34.dp)
                )
                FavoriteButton(
                    isFavorite = hero.isFavorite,
                    onToggle = { viewModel.toggleFavorite(hero.id) },
                    size = 20.dp, // Меньший размер для списка
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size + 16.dp) // Добавляем отступ для обводки
            .border(
                width = 1.dp,
                color = if (isFavorite) Color(COLOR_STAR) else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
    ) {
        IconButton(
            onClick = onToggle,
            modifier = modifier
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color(COLOR_STAR) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(35.dp)
            )

        }

    }
}


@Composable
fun PublisherIcon(
    publisher: String?,
    modifier: Modifier = Modifier.size(24.dp)
) {
    val iconRes = when {
        publisher?.contains("marvel", ignoreCase = true) == true -> R.drawable.ic_marvel
        publisher?.contains("dc", ignoreCase = true) == true -> R.drawable.ic_dc
        else -> R.drawable.ic_publisher
    }

    Icon(
        painter = painterResource(iconRes),
        contentDescription = publisher ?: "Publisher",
        modifier = modifier,
        tint = Color.Unspecified
    )
}

