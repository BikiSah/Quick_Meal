/*
 * RecipeActivity.kt
 * Created by biki.Sah
 */

package com.biki.quickmeal.ui.recipe


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.biki.quickmeal.R
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.ui.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_recipe.toolbar
import javax.inject.Inject


/**
 * Activity to display detail data
 */
class RecipeActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        collapse_tool_bar.setExpandedTitleColor(Color.TRANSPARENT);
        activityComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)
        initAdapter()
        initState()
        (intent.getParcelableExtra<Meal>(EXTRA_MEAL_ID)).apply {
            setData(this)
            if (savedInstanceState == null)
                viewModel.getRecipe(this.id.toInt())

        }
    }

    private fun setData(meal: Meal) {
        supportActionBar?.title = meal.name
        if (meal.thumbnail.isNotEmpty()) {
            Picasso.get().load(meal.thumbnail).into(iv_profile)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initAdapter() {
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.meal.observe(this, Observer {

            val adapter = RecipePagerAdapter(supportFragmentManager)
            adapter.addFragment(
                RecipeIngredientsFragment.newInstance((it.recipeIngredients as ArrayList<RecipeIngredient>?)!!),
                "Ingredients"
            )
            adapter.addFragment(RecipeInstructionsFragment.newInstance(it), "Instructions")
            viewPager.adapter = adapter
            tabs.setupWithViewPager(viewPager)

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
                if (state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (state == State.ERROR) View.VISIBLE else View.GONE

        })
    }

    companion object {

        private const val EXTRA_MEAL_ID = "meal_id"

        fun start(activity: Activity, meal: Meal) {
            activity.startActivity(Intent(activity, RecipeActivity::class.java).apply {
                putExtra(EXTRA_MEAL_ID, meal)
            })
        }
    }
}