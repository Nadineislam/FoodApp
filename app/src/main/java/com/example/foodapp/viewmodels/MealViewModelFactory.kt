package com.example.foodapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.repository.MealRepository
import com.example.foodapp.db.MealDatabase

class MealViewModelFactory(
    private val mealRepository: MealRepository,
    private val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealRepository, mealDatabase) as T
    }

}