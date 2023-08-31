package com.example.foodapp.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.data.utils.Constants.Companion.CATEGORY_NAME
import com.example.foodapp.data.utils.Constants.Companion.MEAL_ID
import com.example.foodapp.data.utils.Constants.Companion.MEAL_NAME
import com.example.foodapp.data.utils.Constants.Companion.MEAL_THUMB
import com.example.foodapp.R
import com.example.foodapp.data.utils.Resource
import com.example.foodapp.presentation.adapters.CategoriesAdapter
import com.example.foodapp.presentation.adapters.PopularItemsAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.data.models.Meal
import com.example.foodapp.presentation.ui.*
import com.example.foodapp.presentation.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: PopularItemsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).homeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingCase()
        preparePopularItemsRecyclerView()
        getRandomMeal()
        onRandomMealClick()
        homeViewModel.getPopularMeals()
        getPopularMeals()
        onPopularItemClick()
        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        getCategories()
        onCategoryClick()
        onSearchIconClick()


    }

    private fun onSearchIconClick() {
        binding.searchIcon.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_searchFragment) }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }


    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)


        }
    }

    private fun preparePopularItemsRecyclerView() {
        popularItemsAdapter = PopularItemsAdapter()
        binding.rvPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun getCategories() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.categories.collect { response ->
                    when (response) {
                        is Resource.Loading -> loadingCase()
                        is Resource.Success -> {
                            response.data?.let { categoryList ->
                                categoriesAdapter.differ.submitList(categoryList.categories)
                            }
                        }
                        is Resource.Error -> {
                            responseCase()
                            response.message?.let { message ->
                                Log.e("Home Fragment", "An error occurred: $message")
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getPopularMeals() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.popularMeals.collect { response ->
                    when (response) {
                        is Resource.Loading -> loadingCase()
                        is Resource.Success -> {
                            responseCase()
                            response.data?.let { categoriesResponse ->
                                popularItemsAdapter.differ.submitList(categoriesResponse.meals)
                            }
                        }
                        is Resource.Error -> {
                            responseCase()
                            response.message?.let { message ->
                                Log.e("Home Fragment", "An error occurred: $message")
                            }
                        }
                    }
                }
            }
        }

    }

    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun getRandomMeal() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
            homeViewModel.randomMeal.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        loadingCase()
                    }
                    is Resource.Success -> {
                        val meal = resource.data
                        Glide.with(this@HomeFragment).load(meal?.strMealThumb)
                            .into(binding.imgRandomMeal)
                        randomMeal=meal as Meal
                        responseCase()
                    }
                    is Resource.Error -> {
                        responseCase()
                        val errorMessage = resource.message
                       Toast.makeText(requireContext(),errorMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }}
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvWhatToEat.visibility = View.INVISIBLE
        binding.imgRandomMeal.visibility = View.INVISIBLE
        binding.tvPopularItems.visibility = View.INVISIBLE
        binding.rvPopularItems.visibility = View.INVISIBLE
        binding.tvCategories.visibility = View.INVISIBLE
        binding.rvCategories.visibility = View.INVISIBLE
    }

    private fun responseCase() {
        binding.tvWhatToEat.visibility = View.VISIBLE
        binding.imgRandomMeal.visibility = View.VISIBLE
        binding.tvPopularItems.visibility = View.VISIBLE
        binding.rvPopularItems.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.rvCategories.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }

}