package com.example.feature_hero_details.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.feature_heroes_list.presentation.viewmodel.HeroesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroDetailScreen(
    heroId: Long,
    viewModel: HeroesListViewModel,
    onBack: () -> Unit
) {
//    val hero by viewModel.getHeroById(heroId).collectAsState(initial = null)
    val hero = viewModel.state.collectAsState().value.heroes.find { it.id == heroId }

//    LaunchedEffect(hero) {
//        Log.d("MyApp:HeroUpdated", "Hero updated: $hero")
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hero Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (hero == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            HeroDetailContent(
                hero = hero!!,
                modifier = Modifier.padding(padding),
                onFavoriteToggle = { viewModel.toggleFavorite(heroId) }
            )
        }
    }
}


