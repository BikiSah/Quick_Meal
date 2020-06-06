package com.biki.quickmeal.ui.ingredients

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.biki.quickmeal.R
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.ui.BaseActivity
import com.biki.quickmeal.ui.ingredients.IngredientsAdapter.Companion.DATA_VIEW_TYPE
import com.biki.quickmeal.ui.ingredients.IngredientsAdapter.Companion.FOOTER_VIEW_TYPE
import com.biki.quickmeal.ui.meals.MealsActivity
import com.biki.quickmeal.ui.search.SearchMealsActivity
import kotlinx.android.synthetic.main.activity_ingredients.*
import kotlinx.android.synthetic.main.content_ingredients_list.*
import javax.inject.Inject

/**
 * Activity to display ingredients in a grid view
 */
class IngredientsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: IngredientsViewModel
    private lateinit var ingredientsListAdapter: IngredientsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredients)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_ingredients)
        activityComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(IngredientsViewModel::class.java)
        initAdapter()
        initState()
    }

    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        ingredientsListAdapter = IngredientsAdapter({ ingredient, imageView ->
            //Opening detail activity. Sending ImageView for shared element transition
            MealsActivity.start(this, ingredient.name)

        }, {
            //On click of retry textview, call the api again
            viewModel.retry()
        })
        recycler_view_ingredients.layoutManager =
            GridLayoutManager(this, 3)

        (recycler_view_ingredients.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (ingredientsListAdapter.getItemViewType(position)) {
                        DATA_VIEW_TYPE -> 1
                        FOOTER_VIEW_TYPE -> 3 //number of columns of the grid
                        else -> -1
                    }
                }
            }
        //set the adapter
        recycler_view_ingredients.adapter = ingredientsListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.ingredients?.observe(this, Observer {
            ingredientsListAdapter.submitList(it)
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
                ingredientsListAdapter.setState(state ?: State.DONE)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_activity_ingredients, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            SearchMealsActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }
}
