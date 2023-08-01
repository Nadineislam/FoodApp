package com.example.foodapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.adapters.PopularItemsAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.models.Meal
import com.example.foodapp.ui.*
import com.example.foodapp.viewmodels.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: PopularItemsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.foodapp.ui.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodapp.ui.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodapp.ui.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodapp.ui.fragments.categoryName"

    }

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
        observeRandomMeal()
        onRandomMealClick()
        homeViewModel.getPopularMeals()
        observePopularMeals()
        onPopularItemClick()
        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observeCategories()
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

    private fun observeCategories() {
        homeViewModel.observeCategoriesLiveData()
            .observe(viewLifecycleOwner, Observer { categories ->
                categoriesAdapter.differ.submitList(categories)

            })
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

    private fun observePopularMeals() {
        homeViewModel.observePopularMealsLiveData().observe(
            viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.differ.submitList(mealList)

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

    private fun observeRandomMeal() {
        homeViewModel.observeRandomMealLiveData().observe(
            viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment).load(meal?.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal
            responseCase()
        }
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