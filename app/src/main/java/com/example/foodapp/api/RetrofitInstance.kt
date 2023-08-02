package com.example.foodapp.api

import com.example.foodapp.utils.Constants.Companion.API_KEY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder().baseUrl(API_KEY)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(MealsApi::class.java)
        }
    }
}