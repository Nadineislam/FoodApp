package com.example.foodapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.utils.Resource
import com.example.foodapp.data.repository.MealRepository
import com.example.foodapp.data.models.MealsByCategoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoryMealsViewModel @Inject constructor(private val mealRepository: MealRepository) :
    ViewModel() {
    private val _categoryMeals: MutableStateFlow<Resource<MealsByCategoryList>> =
        MutableStateFlow(Resource.Loading())
    val categoryMeals:StateFlow<Resource<MealsByCategoryList>> = _categoryMeals


    fun getMealsByCategory(categoryName: String) = viewModelScope.launch {
        _categoryMeals.value=Resource.Loading()
        val response = mealRepository.getMealsByCategory(categoryName)
        _categoryMeals.value=handleCategoryMealsLiveData(response)

    }

    private fun handleCategoryMealsLiveData(response: Response<MealsByCategoryList>): Resource<MealsByCategoryList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}