package com.example.foodapp.repository

import com.example.foodapp.api.ApiService
import com.example.foodapp.db.MealDao
import com.example.foodapp.models.Meal
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val mealDao: MealDao,
    private val apiService: ApiService
) {
    fun getRandomMeal() = apiService.getRandomMeal()
    fun getMealDetails(id: String) = apiService.getMealDetails(id)
    suspend fun getMealsBySearch(mealName: String) = apiService.getMealsBySearch(mealName)
    suspend fun getPopularMeals(categoryName: String) =
        apiService.getPopularMeals(categoryName)

    suspend fun getCategory() = apiService.getCategory()
    suspend fun getMealsByCategory(categoryName: String) =
        apiService.getMealsByCategory(categoryName)

    suspend fun upsertMeal(meal: Meal) = mealDao.upsertMeal(meal)
    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    fun getMeals() = mealDao.getMeals()


}