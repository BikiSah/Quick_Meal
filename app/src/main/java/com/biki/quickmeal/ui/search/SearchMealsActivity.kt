package com.biki.quickmeal.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import com.biki.quickmeal.R
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.ui.BaseActivity
import com.biki.quickmeal.ui.ingredients.IngredientsAdapter
import com.biki.quickmeal.ui.recipe.RecipeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_searched_meals_list.*
import kotlinx.android.synthetic.main.content_searched_meals_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Activity which allows user to search for meals
 */
class SearchMealsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchMealViewModel
    private lateinit var searchedMealsListAdapter: SearchedMealsListAdapter

    private var searchSubscription: Disposable? = null

    private var isQueryChanged: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_meals_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.search_meal)
        activityComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchMealViewModel::class.java)
        recycler_view_searched_meals.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )
        initAdapter()
        initState()
    }

    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        searchedMealsListAdapter = SearchedMealsListAdapter({ meal ->
            //Opening detail activity. Sending ImageView for shared element transition
            RecipeActivity.start(this, meal)

        }, {
            //On click of retry textview call the api again
            viewModel.retry()
        })
        recycler_view_searched_meals.layoutManager =
            GridLayoutManager(this, 3)
        (recycler_view_searched_meals.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (searchedMealsListAdapter.getItemViewType(position)) {
                        IngredientsAdapter.DATA_VIEW_TYPE -> 1
                        IngredientsAdapter.FOOTER_VIEW_TYPE -> 3 //number of columns of the grid
                        else -> -1
                    }
                }
            }
        //set the adapter
        recycler_view_searched_meals.adapter = searchedMealsListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.meals?.observe(this, Observer {
            searchedMealsListAdapter.submitList(if (it.isNotEmpty()) it else null)
            //Workaround to fix this issue
            //https://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position
            if (isQueryChanged) {
                searchedMealsListAdapter.notifyDataSetChanged()
                isQueryChanged = false
            }
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
                searchedMealsListAdapter.setState(state ?: State.DONE)
            }
        })

        searchSubscription =
            et_search_meal.textChanges()
                .skip(1)
                .debounce(
                    500,
                    TimeUnit.MILLISECONDS
                )//deferring the event for sometime as it is expected that user would type in a fast way
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    isQueryChanged = true
                    viewModel.searchQuery.value = it.toString()
                }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        searchSubscription?.dispose()
        super.onDestroy()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SearchMealsActivity::class.java))
        }
    }
}