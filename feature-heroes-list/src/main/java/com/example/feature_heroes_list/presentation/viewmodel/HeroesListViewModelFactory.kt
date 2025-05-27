package com.example.feature_heroes_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.HeroDatabase
import com.example.core.network.api.HeroApiService

class HeroesListViewModelFactory(
    private val database: HeroDatabase,
    private val api: HeroApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeroesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HeroesListViewModel(database, api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


