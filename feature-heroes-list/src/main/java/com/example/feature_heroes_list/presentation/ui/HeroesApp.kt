package com.example.feature_heroes_list.presentation.ui

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HeroesApp( viewModel: HeroesListViewModel) {
//    val navController = rememberNavController()
//
//    // Добавляем системные инсеты
//    Box(modifier = Modifier.fillMaxSize())
//    {
//        NavHost(
//            navController = navController,
//            startDestination = "heroes_list",
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            composable("heroes_list") {
//                HeroesListScreen(
//                    viewModel = viewModel,
//                    onHeroClick = { heroId ->
//                        navController.navigate("hero_details/$heroId")
//                    }
//                )
//            }
//
//            composable(
//                route = "hero_details/{heroId}",
//                arguments = listOf(navArgument("heroId") { type = NavType.LongType })
//            ) { backStackEntry ->
//                val heroId = backStackEntry.arguments?.getLong("heroId") ?: 0L
//                HeroDetailScreen(
//                    heroId = heroId,
//                    viewModel = viewModel,
//                    onBack = { navController.popBackStack() }
//                )
//            }
//
//        }
//    }
//}


