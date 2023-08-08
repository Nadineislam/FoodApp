package com.example.foodapp.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.utils.Constants.Companion.CATEGORY_NAME
import com.example.foodapp.utils.Constants.Companion.MEAL_ID
import com.example.foodapp.utils.Constants.Companion.MEAL_NAME
import com.example.foodapp.utils.Constants.Companion.MEAL_THUMB
import com.example.foodapp.utils.Resource
import com.example.foodapp.adapters.CategoryMealsAdapter
import com.example.foodapp.databinding.ActivityCategoryMealsBinding
import com.example.foodapp.viewmodels.CategoryMealsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    private val categoryMealsViewModel: CategoryMealsViewModel by viewModels()
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecyclerView()

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(CATEGORY_NAME) ?: "")

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { mealsByCategoryList ->
                        hideProgressBar()
                        categoryMealsAdapter.differ.submitList(mealsByCategoryList.meals)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("Home Fragment", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        onCategoryItemClick()
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvCategory.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }

    private fun onCategoryItemClick() {
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)


        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
}