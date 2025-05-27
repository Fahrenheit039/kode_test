package com.example.kode_test


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.HeroDatabase
import com.example.core.network.api.HeroApiService
import com.example.feature_hero_details.presentation.ui.HeroDetailScreen
import com.example.feature_heroes_list.presentation.ui.HeroesListScreen
import com.example.feature_heroes_list.presentation.viewmodel.HeroesListViewModel
import com.example.feature_heroes_list.presentation.viewmodel.HeroesListViewModelFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : ComponentActivity() {

    private lateinit var database: HeroDatabase
    private lateinit var api: HeroApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDatabase()
        setupRetrofit()

        enableEdgeToEdge()

        setContent {
            val windowsInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowsInsetsController?.isAppearanceLightStatusBars = true
            windowsInsetsController?.isAppearanceLightNavigationBars = true

            val viewModel: HeroesListViewModel = viewModel(
                factory = HeroesListViewModelFactory(database, api)
            )
            HeroesApp(viewModel)
        }
    }

    private fun setupDatabase() {
        val driver: SqlDriver = AndroidSqliteDriver(HeroDatabase.Schema, this, "hero.db")
        database = HeroDatabase(driver)
    }
    private fun setupRetrofit() {
        // Настройка Moshi с KotlinJsonAdapterFactory
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        // Настройка Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://superheroapi.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(HeroApiService::class.java)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroesApp( viewModel: HeroesListViewModel) {
    val navController = rememberNavController()

    // Добавляем системные инсеты
    Box(modifier = Modifier.fillMaxSize())
    {
        NavHost(
            navController = navController,
            startDestination = "heroes_list",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            composable("heroes_list") {
                HeroesListScreen(
                    viewModel = viewModel,
                    onHeroClick = { heroId ->
                        navController.navigate("hero_details/$heroId")
                    }
                )
            }

            composable(
                route = "hero_details/{heroId}",
                arguments = listOf(navArgument("heroId") { type = NavType.LongType })
            ) { backStackEntry ->
                val heroId = backStackEntry.arguments?.getLong("heroId") ?: 0L
                HeroDetailScreen(
                    heroId = heroId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

        }
    }
}

