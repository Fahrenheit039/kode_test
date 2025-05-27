// feature-heroes-list/presentation/viewmodel/HeroesListViewModel.kt
package com.example.feature_heroes_list.presentation.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.HeroDatabase
import com.example.core.RESERVE
import com.example.core.database.entities.HeroEntity
import com.example.core.network.api.HeroApiService
import com.example.feature_heroes_list.data.repository.HeroRepositoryImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HeroesListViewModel(
    private val database: HeroDatabase,
    private val api: HeroApiService
) : ViewModel() {
    private val repository = HeroRepositoryImpl(database, api)
    private val _state = MutableStateFlow(HeroesState())
    val state: StateFlow<HeroesState> = _state.asStateFlow()

    private var currentPublisherFilter: String? = null

    fun filterByPublisher(publisher: String?) {
        currentPublisherFilter = publisher?.takeIf { it.isNotBlank() }
        applyFilters()
    }
    private fun applyFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            val allHeroes = database.heroQueries.getAllHeroes().executeAsList()
                .mapNotNull { dbHero -> repository.mapDbHeroToEntity(dbHero) }

            val uniquePublishers = allHeroes
                .mapNotNull { it.biographies.publisher }
                .distinct()
                .sorted()

            _state.update { currentState ->
                currentState.copy(
                    heroes = if (currentPublisherFilter != null) {
                        allHeroes.filter {
                            it.biographies.publisher.equals(currentPublisherFilter, ignoreCase = true)
                        }
                    } else {
                        allHeroes
                    },
                    publishers = uniquePublishers
                )
            }

        }
    }
    fun loadPublishers() {
        viewModelScope.launch {
            val allHeroes = database.heroQueries.getAllHeroes().executeAsList()
                .mapNotNull { dbHero -> repository.mapDbHeroToEntity(dbHero) }

            val publishers = allHeroes
                .mapNotNull { it.biographies.publisher }
                .distinct()
                .sorted()

            _state.update { it.copy(publishers = publishers) }
        }
    }

    fun toggleFavorite(heroId: Long) {
        viewModelScope.launch {
            database.heroQueries.transaction {
                val current = database.heroQueries.selectById(heroId).executeAsOneOrNull()
                current?.let {
                    val newValue = if (it.isFavorite.toInt() == 1) 0 else 1
                    database.heroQueries.updateFavorite(
                        id = heroId,
                        isFavorite = newValue.toLong()
                    )

                    Log.d("MyApp:FavoriteToggle", "Hero ID: $heroId, New Favorite State: ${newValue == 1}")

                    _state.update { currentState ->
                        val index = currentState.heroes.indexOfFirst { hero -> hero.id == heroId }
                        if (index != -1) {
                            // Создаем новый список с обновленным героем
                            val updatedHeroes = currentState.heroes.toMutableList()
                            updatedHeroes[index] = updatedHeroes[index].copy(isFavorite = newValue == 1)
                            currentState.copy(heroes = updatedHeroes)
                        } else {
                            currentState // Если герой не найден, возвращаем текущее состояние
                        }
                    }

                }
            }
        }
    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            if (!loadHeroesFromDb()) {
                loadNextHeroes()
            }
        }
    }

    fun loadNextHeroes() {
        if (_state.value.isLoading) return

        _state.value = _state.value.copy(isLoading = true)

        val startId = _state.value.loadedHeroesCount + 1
        val endId = startId + (RESERVE).toInt()

        viewModelScope.launch (Dispatchers.IO) {
            val newHeroes = mutableListOf<HeroEntity>()
            val deferredHeroes = mutableListOf<Deferred<HeroEntity?>>()

            for (id in startId until endId) {
                val deferred = async {
                    try {
                        val response = api.getHeroById(id.toLong()).execute()
                        if (response.isSuccessful && response.body()?.response == "success") {
                            val heroResponse = response.body()!!
                            repository.insertHeroIfNotExists(database, heroResponse)
                            return@async heroResponse.toHeroEntity()
                        }
                    } catch (e: Exception) {
                        Log.d("MyApp:HeroesListViewModel", "Error loading hero $id", e)
                    }
                    return@async null
                }
                deferredHeroes.add(deferred)
            }

            deferredHeroes.awaitAll().forEach { heroEntity ->
                heroEntity?.let { newHeroes.add(it) }
            }

            _state.update { currentState ->
                currentState.copy(
                    heroes = currentState.heroes + newHeroes,
                    loadedHeroesCount = currentState.loadedHeroesCount + newHeroes.size,
                    isLoading = false
                )
            }

            applyFilters()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun loadHeroesFromDb(): Boolean {
        return try {
            val heroesFromDb = database.heroQueries.getAllHeroes().executeAsList()
                .mapNotNull { dbHero -> repository.mapDbHeroToEntity(dbHero) }

            if (heroesFromDb.isNotEmpty()) {
                _state.update { currentState ->
                    currentState.copy(
                        heroes = heroesFromDb,
                        loadedHeroesCount = heroesFromDb.size
                    )
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("HeroesListViewModel", "Error loading heroes from DB", e)
            false
        }
    }
}


data class HeroesState(
    val heroes: List<HeroEntity> = emptyList(),
    val isLoading: Boolean = false,
    var allHeroesLoaded: Boolean = false,
    val loadedHeroesCount: Int = 0,
    val publishers: List<String> = emptyList()
)


