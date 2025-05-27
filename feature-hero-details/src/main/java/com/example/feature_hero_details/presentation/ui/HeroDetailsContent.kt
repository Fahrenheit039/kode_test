package com.example.feature_hero_details.presentation.ui

//import com.example.feature_hero_details.presentation.viewmodel.HeroDetailsViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.database.entities.AppearancesAfterDb
import com.example.core.database.entities.BiographiesAfterDb
import com.example.core.database.entities.ConnectionsAfterDb
import com.example.core.database.entities.HeroEntity
import com.example.core.database.entities.PowerstatsAfterDb
import com.example.core.database.entities.WorkAfterDb
import com.example.feature_heroes_list.R
import com.example.feature_heroes_list.presentation.ui.FavoriteButton
import com.example.feature_heroes_list.presentation.ui.PublisherIcon

@Composable
fun HeroDetailContent(
    hero: HeroEntity,
    modifier: Modifier = Modifier,
    onFavoriteToggle: () -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            AsyncImage(
                model = hero.images.urls?.firstOrNull(),
                contentDescription = hero.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.error)
            )
        }

        item {
            InfoCard(title="Basic Info") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hero.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = hero.biographies.fullName ?: "Unknown real name",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PublisherIcon(
                            publisher = hero.biographies.publisher,
                            modifier = Modifier.size(50.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        FavoriteButton(
                            isFavorite = hero.isFavorite,
                            onToggle = onFavoriteToggle,
//                                onToggle = {viewModel.toggleFavorite(hero.id)},
                            size = 24.dp, // Больший размер для деталей
                            modifier = Modifier.size(32.dp)
                        )
//                            onToggle = { viewModel.toggleFavorite(hero.id) },

                    }


                }

            }
        }

        // Biography
        item {
            InfoCard(title = "Biography") {
                Text(text = buildBiographyText(hero.biographies))
            }
        }

        // Work
        item {
            InfoCard(title = "Work") {
                Text(text = buildWorkText(hero.work))
            }
        }

        // Powerstats
        item {
            InfoCard(title = "Powerstats") {
                PowerStatsTable(hero.powerstats)
            }
        }

        // Appearance
        item {
            InfoCard(title = "Appearance") {
                AppearanceSection(hero.appearances)
            }
        }

        // Connections
        item {
            InfoCard(title = "Connections") {
                Text(text = buildConnectionsText(hero.connections))
            }
        }

    }
}

// Функции для форматирования текста
@Composable
fun AppearanceSection(appearance: AppearancesAfterDb) {
    Column {
        listOf(
            "Gender" to appearance.gender,
            "Race" to appearance.race,
            "Height" to appearance.height?.joinToString(", "),
            "Weight" to appearance.weight?.joinToString(", "),
            "Eye Color" to appearance.eyeColor,
            "Hair Color" to appearance.hairColor
        ).forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$label:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = value ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
fun buildWorkText(work: WorkAfterDb): String {
    return """
        Occupation: ${work.occupation ?: "Unknown"}
        Base: ${work.base ?: "Unknown"}
    """.trimIndent()
}
fun buildConnectionsText(connections: ConnectionsAfterDb): String {
    return """
        Group Affiliation: ${connections.groupAffiliation ?: "Unknown"}
        Relatives: ${connections.relatives ?: "Unknown"}
    """.trimIndent()
}
@Composable
fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun PowerStatsTable(powerstats: PowerstatsAfterDb) {
    Column {
        PowerStatRow("Intelligence", powerstats.intelligence?:"")
        PowerStatRow("Strength", powerstats.strength?:"")
        PowerStatRow("Speed", powerstats.speed?:"")
        PowerStatRow("Durability", powerstats.durability?:"")
        PowerStatRow("Power", powerstats.power?:"")
        PowerStatRow("Combat", powerstats.combat?:"")
    }
}

@Composable
fun PowerStatRow(statName: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
//            modifier = Modifier.weight(1f),
            modifier = Modifier.width(84.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        LinearProgressIndicator(
            progress = { (value.toIntOrNull() ?: 0) / 100f },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .padding(horizontal = 2.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = value,
            modifier = Modifier.width(30.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )
    }
}

fun buildBiographyText(bio: BiographiesAfterDb): String {
    return buildString {
        append("Full name: ${bio.fullName ?: "Unknown"}\n\n")
        append("Alter egos: ${bio.alterEgos ?: "Unknown"}\n\n")
        append("Aliases: ${bio.aliases?.joinToString(", ") ?: "None"}\n\n")
        append("Place of birth: ${bio.placeOfBirth ?: "Unknown"}\n\n")
        append("First appearance: ${bio.firstAppearance ?: "Unknown"}\n\n")
        append("Publisher: ${bio.publisher ?: "Unknown"}")
    }
}



