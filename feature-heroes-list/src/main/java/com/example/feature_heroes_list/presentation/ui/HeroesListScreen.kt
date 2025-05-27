// feature-heroes-list/presentation/ui/HeroListScreen.kt
package com.example.feature_heroes_list.presentation.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.MAX_HEROES_COUNT
import com.example.core.RESERVE
import com.example.core.USER_NAME
import com.example.core.USER_PROFILE_PIC
import com.example.feature_heroes_list.presentation.viewmodel.HeroesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroesListScreen(
    viewModel: HeroesListViewModel,
    onHeroClick: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val showToast = rememberToast()
    var isFavoritesExpanded by rememberSaveable { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    val selectedPublisher = remember { mutableStateOf<String?>(null) }


    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if ( visibleItems.isNotEmpty() &&
                    visibleItems.last().index >= state.heroes.size - RESERVE &&
                    !state.allHeroesLoaded
                ) {
                    Log.d("MyApp:LoadingStart","isLoading = ${visibleItems.isNotEmpty()}" +
                            " || ${visibleItems.last().index}(lastVisible) >= ${state.heroes.size- RESERVE}" +
                            "(totalItemCount(${state.heroes.size}) - RESERVE($RESERVE))")
                    if (state.loadedHeroesCount < MAX_HEROES_COUNT) {
                        viewModel.loadNextHeroes()
                    }else{
                        Log.d("MyApp:LoadingStop", "All heroes have been loaded.")
                        state.allHeroesLoaded = true
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            Column (modifier = Modifier.padding(horizontal = 16.dp)) {
                ProfileTopAppBar(
                    title = "Superheroes",
                    onMenuClick = { showToast("Menu clicked") }
                )

                // Фильтр по издателю - material3
                Box(
                    modifier = Modifier.fillMaxWidth()
                ){
                    // Поле для отображения выбранного значения
                    OutlinedTextField(
                        value = selectedPublisher.value ?: "All Comics",
//                            enabled = false,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded) Icons.Default.ArrowDropDownCircle else Icons.Default.ArrowDropDown,
                                contentDescription = "Toggle dropdown",
                            )
                        },
//                            modifier = Modifier.fillMaxWidth(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                awaitEachGesture {
                                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                                    // in the Initial pass to observe events before the text field consumes them
                                    // in the Main pass.
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        expanded = true
                                        viewModel.loadPublishers()
                                    }
                                }
                            },
                        label = { Text("Filter by publisher") },
                    )

                    // Выпадающее меню
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(fraction = 0.9f)
                    ) {
                        // Опция "Все издатели"
                        DropdownMenuItem(
                            text = { Text("All Comics") },
                            onClick = {
                                selectedPublisher.value = null
                                viewModel.filterByPublisher(null)
                                expanded = false
                            }
                        )

                        // Показываем загрузку если publishers еще не загружены
                        if (state.publishers.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                onClick = {}
                            )
                        } else {
                            // Опции для каждого издателя
                            state.publishers.forEach { publisher ->
                                DropdownMenuItem(
                                    text = { Text(publisher) },
                                    onClick = {
                                        selectedPublisher.value = publisher
                                        viewModel.filterByPublisher(publisher)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp), // Единые отступы
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            // Сначала показываем избранных
            val favorites = state.heroes.filter { it.isFavorite }
            if (favorites.isNotEmpty()) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp)
                            .padding(top = 2.dp)
                            .clickable { isFavoritesExpanded = !isFavoritesExpanded }
                    ) {
                        Text(
                            text = "Favorites",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (isFavoritesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isFavoritesExpanded) "Collapse" else "Expand",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = isFavoritesExpanded && favorites.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            favorites.forEach { hero ->
                                HeroItem(
                                    hero = hero,
                                    onClick = { onHeroClick(hero.id) },
                                    viewModel = viewModel
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "All Heroes",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp)
                            .padding(top = 2.dp)
                    )
                }
            }

            // Затем всех остальных
            items(state.heroes.filter { !it.isFavorite }) { hero ->
                HeroItem(
                    hero = hero,
                    onClick = { onHeroClick(hero.id) },
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (state.isLoading) {
                item {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    title: String,
    onMenuClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {


                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = USER_NAME,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        },
        navigationIcon = {
            AsyncImage(
                model = USER_PROFILE_PIC, // Замените на реальный URL
                contentDescription = "Profile",
                modifier = Modifier
//                    .padding(8.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            )
        },
        actions = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
//                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}



@Composable
fun rememberToast(): (String) -> Unit {
    val context = LocalContext.current
    return { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}


