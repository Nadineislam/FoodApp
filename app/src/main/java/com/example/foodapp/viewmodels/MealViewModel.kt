package com.example.foodapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.api.RetrofitInstance
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.models.Meal
import com.example.foodapp.models.MealList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MealViewModel(private val mealDatabase: MealDatabase):ViewModel() {
    private var mealDetailsLiveData=MutableLiveData<Meal>()
    fun getMealDetails(id:String)=RetrofitInstance.api.getMealDetails(id).enqueue(object :retrofit2.Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            if(response.body()!=null){
                mealDetailsLiveData.value=response.body()!!.meals[0]
            }
            else{return}
        }

        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.e("Meal Activity",t.message.toString())
        }

    })
    fun observeMealDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

}