package com.example.foodapp.data.api

import com.example.foodapp.data.models.CategoryList
import com.example.foodapp.data.models.MealsByCategoryList
import com.example.foodapp.data.models.MealList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
   suspend fun getRandomMeal(): Response<MealList>

    @GET("lookup.php?")
    suspend fun getMealDetails(@Query("i") id: String): Response<MealList>

    @GET("filter.php")
    suspend fun getPopularMeals(@Query("c") categoryName: String): Response<MealsByCategoryList>

    @GET("categories.php")
    suspend fun getCategory(): Response<CategoryList>

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") categoryName: String): Response<MealsByCategoryList>

    @GET("search.php")
    suspend fun getMealsBySearch(@Query("s") mealName: String): Response<MealList>
}