package com.example.mobilecookbook

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeListFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private lateinit var addButton: Button
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        // Inicjalizacja widoków (teraz poprawnie)
        recyclerView = view.findViewById(R.id.recyclerView)
        addButton = view.findViewById(R.id.button_add_recipe)

        recyclerView?.layoutManager = LinearLayoutManager(context)
        sharedPreferences = requireActivity().getSharedPreferences("MyCookbook", Context.MODE_PRIVATE)

        // Załadowanie przepisów jako MutableList
        val recipes = loadRecipes().toMutableList()  // KONWERSJA na MutableList

        // Inicjalizacja adaptera
        recipeAdapter = RecipeAdapter(recipes) { recipe ->
            val fragment = RecipeDetailsFragment.newInstance(recipe)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView?.adapter = recipeAdapter  // Używamy `?` na wypadek null

        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddRecipeFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loadRecipes(): List<Recipe> {
        val sharedPreferences = requireActivity().getSharedPreferences("MyCookbook", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("recipes", null)
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
