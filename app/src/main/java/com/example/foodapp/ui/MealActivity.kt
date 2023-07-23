package com.example.foodapp.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.models.Meal
import com.example.foodapp.ui.fragments.HomeFragment
import com.example.foodapp.viewmodels.MealViewModel
import com.example.foodapp.viewmodels.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    lateinit var binding:ActivityMealBinding
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var mealViewModel: MealViewModel
    private lateinit var youtubeLink:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelFactory= MealViewModelFactory(mealDatabase)
        mealViewModel=ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
        getMealInformation()
        setMealInformation()
        loadingCase()
        mealViewModel.getMealDetails(mealId)
        observeMealDetailsLiveData()
        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.fab.setOnClickListener {
            savedMeal?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this,"Meal saved",Toast.LENGTH_LONG).show()
            }
        }
    }
private var savedMeal:Meal?=null
    private fun observeMealDetailsLiveData() {
        mealViewModel.observeMealDetailsLiveData().observe(this
        ) { t ->
            val meal = t
            savedMeal = meal
            binding.tvCategory.text = meal?.strCategory.toString()
            binding.tvArea.text = meal?.strArea.toString()
            binding.tvInstructionsDesc.text = meal?.strInstructions.toString()
            youtubeLink = meal?.strYoutube.toString()
            onResponseCase()
        }
    }

    private fun setMealInformation() {
        Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDetails)
        binding.collapsingToolBar.title=mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(ContextCompat.getColor(applicationContext,R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(ContextCompat.getColor(applicationContext,R.color.white))
    }

    private fun getMealInformation() {
        val intent=intent
       mealName= intent.getStringExtra(HomeFragment.MEAL_NAME)!!
       mealId= intent.getStringExtra(HomeFragment.MEAL_ID)!!
       mealThumb= intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
    private fun loadingCase(){
        binding.tvCategory.visibility=View.INVISIBLE
        binding.tvArea.visibility=View.INVISIBLE
        binding.tvInstructionsDesc.visibility=View.INVISIBLE
        binding.progressBar.visibility=View.VISIBLE
        binding.imgYoutube.visibility=View.INVISIBLE
        binding.fab.visibility=View.INVISIBLE
    }
    private fun onResponseCase(){
        binding.tvCategory.visibility=View.VISIBLE
        binding.tvArea.visibility=View.VISIBLE
        binding.tvInstructionsDesc.visibility=View.VISIBLE
        binding.progressBar.visibility=View.INVISIBLE
        binding.imgYoutube.visibility=View.VISIBLE
        binding.fab.visibility=View.VISIBLE
    }
    private fun onYoutubeImageClick(){
        binding.imgYoutube.setOnClickListener { val intent=Intent(Intent.ACTION_VIEW,Uri.parse(youtubeLink))
        startActivity(intent)}
    }
}