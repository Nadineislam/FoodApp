package com.example.foodapp.data.repository

import com.example.foodapp.data.api.ApiService
import com.example.foodapp.data.db.MealDao
import com.example.foodapp.data.models.Meal
import javax.inject.Inject

class MealsRepository @Inject constructor(
    private val mealDao: MealDao,
    private val apiService: ApiService
) {
    suspend fun getRandomMeal() = apiService.getRandomMeal()
    suspend fun getMealDetails(id: String) = apiService.getMealDetails(id)
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