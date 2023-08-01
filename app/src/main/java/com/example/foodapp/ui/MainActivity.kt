package com.example.foodapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodapp.R
import com.example.foodapp.repository.MealRepository
import com.example.foodapp.databinding.ActivityMainBinding
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.viewmodels.HomeViewModel
import com.example.foodapp.viewmodels.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val homeViewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val mealRepository = MealRepository()
        val homeViewModelFactory = HomeViewModelFactory(mealRepository, mealDatabase)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.foodNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)

    }
}