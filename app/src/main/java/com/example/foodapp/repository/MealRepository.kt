package com.example.foodapp.repository

import com.example.foodapp.api.RetrofitInstance

class MealRepository {
    fun getRandomMeal() = RetrofitInstance.api.getRandomMeal()
    fun getMealDetails(id: String) = RetrofitInstance.api.getMealDetails(id)
    fun getMealsBySearch(mealName: String) = RetrofitInstance.api.getMealsBySearch(mealName)
    fun getPopularMeals(categoryName: String) = RetrofitInstance.api.getPopularMeals(categoryName)
    fun getCategory() = RetrofitInstance.api.getCategory()
    fun getMealsByCategory(categoryName: String) =
        RetrofitInstance.api.getMealsByCategory(categoryName)

}