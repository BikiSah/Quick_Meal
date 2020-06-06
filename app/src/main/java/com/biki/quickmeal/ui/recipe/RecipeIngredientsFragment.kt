package com.biki.quickmeal.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.biki.quickmeal.R
import kotlinx.android.synthetic.main.fragment_ingredients.*

/**
 * Fragment responsible for displaying recipe ingredients in a grid view when user selects a recipe
 */
class RecipeIngredientsFragment : Fragment() {

    private lateinit var recipeIngredientsAdapter: RecipeIngredientsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelableArrayList<RecipeIngredient>("ingredients")?.apply {
            initAdapter(this.toList())
        }
    }

    /**
     * Initializing the adapter
     */
    private fun initAdapter(recipeIngredients: List<RecipeIngredient>) {
        recipeIngredientsAdapter = RecipeIngredientsAdapter(recipeIngredients)
        recycler_view_recipe_ingredients?.layoutManager =
            GridLayoutManager(activity, 3)

        //set the adapter
        recycler_view_recipe_ingredients?.adapter = recipeIngredientsAdapter
    }

    companion object {
        fun newInstance(recipeIngredients: ArrayList<RecipeIngredient>): Fragment {
            val fragment = RecipeIngredientsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("ingredients", recipeIngredients)
            fragment.arguments = bundle
            return fragment
        }
    }


}