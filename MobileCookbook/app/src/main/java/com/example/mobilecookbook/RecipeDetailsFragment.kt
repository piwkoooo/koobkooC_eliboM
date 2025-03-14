package com.example.mobilecookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeDetailsFragment : Fragment() {
    private lateinit var recipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)
        val nameText = view.findViewById<TextView>(R.id.text_recipe_name)
        val ingredientsText = view.findViewById<TextView>(R.id.text_ingredients)
        val instructionsText = view.findViewById<TextView>(R.id.text_instructions)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)

        nameText.text = recipe.name
        ingredientsText.text = recipe.ingredients
        instructionsText.text = recipe.instructions
        ratingBar.rating = recipe.rating

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            recipe.rating = rating
            saveUpdatedRecipe()
        }

        return view
    }

    private fun saveUpdatedRecipe() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyCookbook", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val recipes = loadRecipes().map {
            if (it.name == recipe.name) recipe else it
        }
        editor.putString("recipes", Gson().toJson(recipes))
        editor.apply()
    }

    private fun loadRecipes(): List<Recipe> {
        val sharedPreferences = requireActivity().getSharedPreferences("MyCookbook", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("recipes", null)
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    companion object {
        fun newInstance(recipe: Recipe): RecipeDetailsFragment {
            val fragment = RecipeDetailsFragment()
            fragment.recipe = recipe
            return fragment
        }
    }
}
