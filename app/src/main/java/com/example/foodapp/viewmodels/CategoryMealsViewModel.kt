package com.example.foodapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.utils.Resource
import com.example.foodapp.repository.MealRepository
import com.example.foodapp.models.MealsByCategoryList
import kotlinx.coroutines.launch
import retrofit2.Response

class CategoryMealsViewModel(private val mealRepository: MealRepository) : ViewModel() {
    private val categoryMealsLiveData: MutableLiveData<Resource<MealsByCategoryList>> =
        MutableLiveData()


    fun getMealsByCategory(categoryName: String) = viewModelScope.launch {
        categoryMealsLiveData.postValue(Resource.Loading())
        val response = mealRepository.getMealsByCategory(categoryName)
        categoryMealsLiveData.postValue(handleCategoryMealsLiveData(response))

    }

    private fun handleCategoryMealsLiveData(response: Response<MealsByCategoryList>): Resource<MealsByCategoryList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun observeMealsLiveData(): LiveData<Resource<MealsByCategoryList>> {
        return categoryMealsLiveData
    }

}