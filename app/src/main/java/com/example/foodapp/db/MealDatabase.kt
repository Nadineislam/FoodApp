package com.example.foodapp.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapp.models.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}
