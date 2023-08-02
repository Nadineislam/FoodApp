package com.example.foodapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.utils.Resource
import com.example.foodapp.repository.MealRepository
import com.example.foodapp.models.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealRepository: MealRepository
) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealsLiveData: MutableLiveData<Resource<MealsByCategoryList>> =
        MutableLiveData()
    private var categoriesLiveData: MutableLiveData<Resource<CategoryList>> = MutableLiveData()
    private var favoritesMealsLiveData = mealRepository.getMeals()

    private var searchMealLiveData = MutableLiveData<Resource<MealList>>()

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

    fun getCategories() = viewModelScope.launch {
        categoriesLiveData.postValue(Resource.Loading())
        val response = mealRepository.getCategory()
        categoriesLiveData.postValue(handleCategoriesResponse(response))
    }

    private fun handleCategoriesResponse(response: Response<CategoryList>): Resource<CategoryList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getPopularMeals() = viewModelScope.launch {
        popularMealsLiveData.postValue(Resource.Loading())
        val response = mealRepository.getPopularMeals("Seafood")
        popularMealsLiveData.postValue(handlePopularMealsResponse(response))
    }

    private fun handlePopularMealsResponse(response: Response<MealsByCategoryList>): Resource<MealsByCategoryList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchMeal(searchQuery: String) = viewModelScope.launch {
        searchMealLiveData.postValue(Resource.Loading())
        val response = mealRepository.getMealsBySearch(searchQuery)
        searchMealLiveData.postValue(handleSearchMealResponse(response))
    }

    private fun handleSearchMealResponse(response: Response<MealList>): Resource<MealList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealRepository.upsertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealRepository.deleteMeal(meal)
        }
    }

    fun observeSearchMealsLiveData(): LiveData<Resource<MealList>> {
        return searchMealLiveData
    }

    fun observePopularMealsLiveData(): LiveData<Resource<MealsByCategoryList>> {
        return popularMealsLiveData
    }


    fun observeCategoriesLiveData(): LiveData<Resource<CategoryList>> {
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }
}