package com.example.foodapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.repository.MealRepository

class CategoryMealsViewModelFactory(private val mealRepository: MealRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryMealsViewModel(mealRepository) as T

    }
}