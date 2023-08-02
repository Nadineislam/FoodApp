package com.example.foodapp.api

import com.example.foodapp.models.CategoryList
import com.example.foodapp.models.MealsByCategoryList
import com.example.foodapp.models.MealList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    suspend fun getPopularMeals(@Query("c") categoryName: String): Response<MealsByCategoryList>

    @GET("categories.php")
    suspend fun getCategory(): Response<CategoryList>

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") categoryName: String): Response<MealsByCategoryList>

    @GET("search.php")
    suspend fun getMealsBySearch(@Query("s") mealName: String): Response<MealList>
}