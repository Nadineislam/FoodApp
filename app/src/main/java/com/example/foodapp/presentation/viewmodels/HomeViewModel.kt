package com.example.foodapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.utils.Resource
import com.example.foodapp.data.repository.MealsRepository
import com.example.foodapp.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealRepository: MealsRepository
) : ViewModel() {
    private val _randomMeal: MutableStateFlow<Resource<Meal>> = MutableStateFlow(Resource.Loading())
    val randomMeal: StateFlow<Resource<Meal>> = _randomMeal

    private val _popularMeals: MutableStateFlow<Resource<MealsByCategoryList>> =
        MutableStateFlow(Resource.Loading())
    val popularMeals: StateFlow<Resource<MealsByCategoryList>> = _popularMeals

    private val _categories: MutableStateFlow<Resource<CategoryList>> =
        MutableStateFlow(Resource.Loading())
    val categories: StateFlow<Resource<CategoryList>> = _categories

    private var favoritesMealsLiveData = mealRepository.getMeals()

    private val _searchMeal = MutableStateFlow<Resource<MealList>>(Resource.Loading())
    val searchMeal: StateFlow<Resource<MealList>> = _searchMeal

    init {
        getRandomMeal()
    }

    private fun getRandomMeal() = viewModelScope.launch {
        _randomMeal.value = Resource.Loading()
        val response = mealRepository.getRandomMeal()
        if (response.isSuccessful) {
            val mealList = response.body()
            val randomMeal = mealList?.meals?.firstOrNull()
            _randomMeal.value = Resource.Success(randomMeal as Meal)
        } else {
            val errorMessage = response.message()
            _randomMeal.value = Resource.Error(errorMessage)
        }

    }

    fun getCategories() = viewModelScope.launch {
        _categories.value = Resource.Loading()
        val response = mealRepository.getCategory()
        _categories.value = handleCategoriesResponse(response)
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
        _popularMeals.value = Resource.Loading()
        val response = mealRepository.getPopularMeals("Seafood")
        _popularMeals.value = handlePopularMealsResponse(response)
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
        _searchMeal.value = Resource.Loading()
        val response = mealRepository.getMealsBySearch(searchQuery)
        _searchMeal.value = handleSearchMealResponse(response)
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

    fun observeFavoritesMealsStateFlow(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }
}