package com.example.foodapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.repository.MealRepository
import com.example.foodapp.models.MealsByCategory
import com.example.foodapp.models.MealsByCategoryList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel(private val mealRepository: MealRepository) : ViewModel() {
    private val categoryMealsLiveData = MutableLiveData<List<MealsByCategory>>()
    fun getMealsByCategory(categoryName: String) = mealRepository.getMealsByCategory(categoryName)
        .enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let { mealsList ->
                    categoryMealsLiveData.value = mealsList.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryMealsViewModel", t.message.toString())
            }

        })

    fun observeMealsLiveData(): LiveData<List<MealsByCategory>> {
        return categoryMealsLiveData
    }
}