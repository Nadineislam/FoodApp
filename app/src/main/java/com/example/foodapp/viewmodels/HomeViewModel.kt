package com.example.foodapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.repository.MealRepository
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.models.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealRepository: MealRepository,
    private val mealDatabase: MealDatabase
) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getMeals()
    private var searchMealLiveData = MutableLiveData<List<Meal>>()

    init {
        getRandomMeal()
    }

    private fun getRandomMeal() =
        mealRepository.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("TEST", t.message.toString())
            }

        })

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun getPopularMeals() =
        mealRepository.getPopularMeals("Seafood").enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {

                popularMealsLiveData.value = response.body()?.meals

            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("Home Fragment", t.message.toString())
            }


        })

    fun searchMeal(searchQuery: String) {
        mealRepository.getMealsBySearch(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let { searchMealLiveData.postValue(it) }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("Home Fragment", t.message.toString())
            }

        })

    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun observeSearchMealsLiveData(): LiveData<List<Meal>> {
        return searchMealLiveData
    }

    fun observePopularMealsLiveData(): LiveData<List<MealsByCategory>> {
        return popularMealsLiveData
    }

    fun getCategories() = mealRepository.getCategory().enqueue(object : Callback<CategoryList> {
        override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
            response.body()?.let { categoryList ->
                categoriesLiveData.value = categoryList.categories
            }
        }

        override fun onFailure(call: Call<CategoryList>, t: Throwable) {
            Log.e("Home Fragment", t.message.toString())
        }

    })

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }
}