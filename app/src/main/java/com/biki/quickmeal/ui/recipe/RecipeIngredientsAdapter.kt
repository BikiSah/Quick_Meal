

package com.biki.quickmeal.ui.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.biki.quickmeal.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_ingredient.view.*


/**
 * Adapter responsible for binding the list of recipe ingredients to RecyclerView
 */
class RecipeIngredientsAdapter(private val recipeIngredients: List<RecipeIngredient>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeIngredientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecipeIngredientViewHolder).bind(recipeIngredients[position])

    }


    override fun getItemCount(): Int {
        return recipeIngredients.size
    }
}

/**
 * ViewHolder to display meal information
 */
class RecipeIngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        recipeIngredient: RecipeIngredient
    ) {
        with(recipeIngredient) {
            itemView.txt_name.text = "$name ($quantity)"
            Picasso.get()
                .load("https://www.themealdb.com/images/ingredients/${name}-Small.png")
                .into(itemView.iv_thumbnail)
        }


    }

    companion object {
        fun create(parent: ViewGroup): RecipeIngredientViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_recipe_ingredient, parent, false)
            return RecipeIngredientViewHolder(view)
        }
    }
}
