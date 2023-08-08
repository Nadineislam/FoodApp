package com.example.foodapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodapp.api.ApiService
import com.example.foodapp.db.MealDao
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.repository.MealRepository
import com.example.foodapp.utils.Constants.Companion.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context):MealDatabase =
        Room.databaseBuilder(context,MealDatabase::class.java,"postDatabase")
            .build()

    @Provides
    fun providesPostDao(mealDatabase: MealDatabase):MealDao =
        mealDatabase.mealDao()
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(API_KEY)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesRepository(mealDao: MealDao,apiService: ApiService) = MealRepository(mealDao,apiService)



}