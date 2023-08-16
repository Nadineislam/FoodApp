package com.example.foodapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.utils.Resource
import com.example.foodapp.data.repository.MealRepository
import com.example.foodapp.data.models.MealsByCategoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoryMealsViewModel @Inject constructor(private val mealRepository: MealRepository) :
    ViewModel() {
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