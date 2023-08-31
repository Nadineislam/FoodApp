package com.example.foodapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodapp.data.models.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM MealTable")
    fun getMeals(): LiveData<List<Meal>>

}