package com.example.mobilecookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddRecipeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)
        val nameInput = view.findViewById<EditText>(R.id.edit_recipe_name)
        val ingredientsInput = view.findViewById<EditText>(R.id.edit_ingredients)
        val instructionsInput = view.findViewById<EditText>(R.id.edit_instructions)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val saveButton = view.findViewById<Button>(R.id.button_save)

        saveButton.setOnClickListener {
            val recipe = Recipe(
                nameInput.text.toString(),
                ingredientsInput.text.toString(),
                instructionsInput.text.toString(),
                ratingBar.rating
            )
            saveRecipe(recipe)
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun saveRecipe(recipe: Recipe) {
        val sharedPreferences = requireActivity().getSharedPreferences("MyCookbook", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val recipes = loadRecipes().apply { add(recipe) }
        editor.putString("recipes", Gson().toJson(recipes))
        editor.apply()
    }

    private fun loadRecipes(): MutableList<Recipe> {
        val sharedPreferences = requireActivity().getSharedPreferences("MyCookbook", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("recipes", "[]")
        val type = object : TypeToken<MutableList<Recipe>>() {}.type
        return Gson().fromJson(json, type) ?: mutableListOf()
    }
}