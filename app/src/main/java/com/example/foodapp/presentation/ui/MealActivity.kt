package com.example.foodapp.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodapp.data.utils.Constants.Companion.MEAL_ID
import com.example.foodapp.data.utils.Constants.Companion.MEAL_NAME
import com.example.foodapp.data.utils.Constants.Companion.MEAL_THUMB
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.data.models.Meal
import com.example.foodapp.data.utils.Resource
import com.example.foodapp.presentation.viewmodels.MealViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MealActivity : AppCompatActivity() {
    lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String

    private val mealViewModel: MealViewModel by viewModels()
    private lateinit var youtubeLink: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMealInformation()
        setMealInformation()
        loadingCase()
        mealViewModel.getMealDetails(mealId)
        getMealDetails()
        onYoutubeImageClick()
        onFavoriteClick()
    }

    private var savedMeal: Meal? = null
    private fun getMealDetails() {
        lifecycleScope.launch {
            mealViewModel.mealDetails.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        loadingCase()
                    }
                    is Resource.Success -> {
                        onResponseCase()
                        val mealList = resource.data
                        savedMeal = mealList?.meals?.firstOrNull()
                        // Update UI with the meal details
                        binding.tvCategory.text = savedMeal?.strCategory.toString()
                        binding.tvArea.text = savedMeal?.strArea.toString()
                        binding.tvInstructionsDesc.text = savedMeal?.strInstructions.toString()
                        youtubeLink = savedMeal?.strYoutube.toString()
                        onResponseCase()
                    }
                    is Resource.Error -> {
                        onResponseCase()
                        val errorMessage = resource.message
                        Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun onFavoriteClick() {
        binding.fab.setOnClickListener {
            savedMeal?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(baseContext, "Meal saved", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setMealInformation() {
        Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDetails)
        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.collapsingToolBar.setExpandedTitleColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
    }

    private fun getMealInformation() {
        val intent = intent
        mealName = intent.getStringExtra(MEAL_NAME) ?: ""
        mealId = intent.getStringExtra(MEAL_ID) ?: ""
        mealThumb = intent.getStringExtra(MEAL_THUMB) ?: ""
    }

    private fun loadingCase() {
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvInstructionsDesc.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
        binding.fab.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvInstructionsDesc.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }
}