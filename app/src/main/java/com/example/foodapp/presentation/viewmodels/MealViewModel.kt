package com.example.foodapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.repository.MealRepository
import com.example.foodapp.data.models.Meal
import com.example.foodapp.data.models.MealList
import com.example.foodapp.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {
    private val _mealDetails: MutableStateFlow<Resource<MealList>> = MutableStateFlow(Resource.Loading())
    val mealDetails: StateFlow<Resource<MealList>> = _mealDetails

   fun getMealDetails(id:String) = viewModelScope.launch {
       _mealDetails.value=Resource.Loading()
       val response = mealRepository.getMealDetails(id)
       _mealDetails.value=handleMealDetailsResponse(response)
   }

    private fun handleMealDetailsResponse(response: Response<MealList>): Resource<MealList> {
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

}