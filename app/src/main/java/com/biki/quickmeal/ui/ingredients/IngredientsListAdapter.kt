

package com.biki.quickmeal.ui.ingredients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.biki.quickmeal.R
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.ui.view.ListFooterViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_ingredient.view.*

/**
 * Adapter responsible for binding the list of ingredients to RecyclerView
 */
class IngredientsAdapter(
    private val onItemClick: (Ingredient, ImageView) -> Unit,
    private val retry: () -> Unit
) : PagedListAdapter<Ingredient, RecyclerView.ViewHolder>(diffCallback) {


    private var state = State.LOADING

    companion object {
        val DATA_VIEW_TYPE = 1
        val FOOTER_VIEW_TYPE = 2
        /**
         * DiffUtils is used improve the performance by finding difference between two lists and updating only the new items
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    private val onItemClickListener = View.OnClickListener {
        val ingredient = it.tag as Ingredient
        onItemClick.invoke(ingredient, it.iv_thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) IngredientViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as IngredientViewHolder).bind(getItem(position)!!, onItemClickListener)
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}

/**
 * ViewHolder to display ingredient information
 */
class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        ingredient: Ingredient,
        onItemClickListener: View.OnClickListener
    ) {
        with(ingredient) {
            itemView.txt_name.text = name
            Picasso.get()
                .load("https://www.themealdb.com/images/ingredients/${name}-Small.png")
                .into(itemView.iv_thumbnail)
        }

        itemView.tag = ingredient
        itemView.setOnClickListener(onItemClickListener)
    }

    companion object {
        fun create(parent: ViewGroup): IngredientViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_ingredient, parent, false)
            return IngredientViewHolder(view)
        }
    }
}
