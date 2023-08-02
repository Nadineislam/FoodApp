package com.example.foodapp.repository

import com.example.foodapp.api.RetrofitInstance
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.models.Meal

class MealRepository(private val mealDatabase: MealDatabase) {
    fun getRandomMeal() = RetrofitInstance.api.getRandomMeal()
    fun getMealDetails(id: String) = RetrofitInstance.api.getMealDetails(id)
    suspend fun getMealsBySearch(mealName: String) = RetrofitInstance.api.getMealsBySearch(mealName)
    suspend fun getPopularMeals(categoryName: String) =
        RetrofitInstance.api.getPopularMeals(categoryName)

    suspend fun getCategory() = RetrofitInstance.api.getCategory()
    suspend fun getMealsByCategory(categoryName: String) =
        RetrofitInstance.api.getMealsByCategory(categoryName)

    suspend fun upsertMeal(meal: Meal) = mealDatabase.mealDao().upsertMeal(meal)
    suspend fun deleteMeal(meal: Meal) = mealDatabase.mealDao().deleteMeal(meal)
    fun getMeals() = mealDatabase.mealDao().getMeals()


}