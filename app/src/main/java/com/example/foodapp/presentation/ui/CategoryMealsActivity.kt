package com.example.foodapp.presentation.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.data.utils.Constants.Companion.CATEGORY_NAME
import com.example.foodapp.data.utils.Constants.Companion.MEAL_ID
import com.example.foodapp.data.utils.Constants.Companion.MEAL_NAME
import com.example.foodapp.data.utils.Constants.Companion.MEAL_THUMB
import com.example.foodapp.data.utils.Resource
import com.example.foodapp.presentation.adapters.CategoryMealsAdapter
import com.example.foodapp.databinding.ActivityCategoryMealsBinding
import com.example.foodapp.presentation.viewmodels.CategoryMealsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryMealsViewModel.categoryMeals.collect { response ->
                    when (response) {
                        is Resource.Loading -> showProgressBar()
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
                    }
                }
            }
        }

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