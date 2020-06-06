package com.biki.quickmeal.ui.meals

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.biki.quickmeal.R
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.ui.BaseActivity
import com.biki.quickmeal.ui.ingredients.IngredientsAdapter.Companion.DATA_VIEW_TYPE
import com.biki.quickmeal.ui.ingredients.IngredientsAdapter.Companion.FOOTER_VIEW_TYPE
import com.biki.quickmeal.ui.recipe.RecipeActivity
import kotlinx.android.synthetic.main.activity_meals.*
import kotlinx.android.synthetic.main.content_meals_list.*
import javax.inject.Inject

/**
 * Activity responsible for displaying list of meals after selecting a ingredient from the IngredientsActivity screen.
 */
class MealsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MealsViewModel
    private lateinit var mealsListAdapter: MealsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        activityComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MealsViewModel::class.java)
        initAdapter()
        initState()
        (intent.getStringExtra(EXTRA_INGREDIENT_NAME)).apply {
            supportActionBar?.title = "$this Recipes"

            if (savedInstanceState == null)
                viewModel.ingredient.value = this

        }
    }

    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        mealsListAdapter = MealsListAdapter({ meal, imageView ->
            //Opening detail activity. Sending ImageView for shared element transition
            RecipeActivity.start(this, meal)
        }, {
            //On click of retry textview call the api again
            viewModel.retry()
        })
        recycler_view_meals.layoutManager =
            GridLayoutManager(this, 3)

        (recycler_view_meals.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (mealsListAdapter.getItemViewType(position)) {
                        DATA_VIEW_TYPE -> 1
                        FOOTER_VIEW_TYPE -> 3 //number of columns of the grid
                        else -> -1
                    }
                }
            }
        //set the adapter
        recycler_view_meals.adapter = mealsListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.meals?.observe(this, Observer {
            mealsListAdapter.submitList(it)
        })
    }

    /**
     * Initializing the state
     */
    private fun initState() {
        //On click of retry textview call the api again
        txt_error.setOnClickListener { viewModel.retry() }
        //Observing the different states of the API calling, and updating the UI accordingly
        viewModel.state.observe(this, Observer { state ->
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                mealsListAdapter.setState(state ?: State.DONE)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        private const val EXTRA_INGREDIENT_NAME = "ingredient_name"
        fun start(activity: Activity, ingredientName: String) {

            activity.startActivity(Intent(activity, MealsActivity::class.java).apply {
                putExtra(EXTRA_INGREDIENT_NAME, ingredientName)
            })
        }
    }
}
