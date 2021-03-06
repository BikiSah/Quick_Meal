package com.biki.quickmeal.ui.meals

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.pagination.factory.MealsPagingDataSourceFactory
import com.biki.quickmeal.ui.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MealsViewModel @Inject constructor(private val mealsDataSourceFactory: MealsPagingDataSourceFactory) :
    BaseViewModel() {
    //LiveData object for Meals
    var meals: LiveData<PagedList<Meal>>? = null
    //LiveData object for state
    var state = mealsDataSourceFactory.mealsPagingDataSource.state
    var ingredient = mealsDataSourceFactory.mealsPagingDataSource.ingredient

    init {
        //Setting up Paging for fetching the meals in pagination
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        meals = Transformations.switchMap(ingredient) { _ ->
            LivePagedListBuilder<Int, Meal>(
                mealsDataSourceFactory, config
            ).build()
        }
    }

    fun listIsEmpty(): Boolean {
        return meals?.value?.isEmpty() ?: true
    }

    //Retrying the API call
    fun retry() {
        mealsDataSourceFactory.mealsPagingDataSource.retry()
    }

    override var disposable: CompositeDisposable
        get() = mealsDataSourceFactory.mealsPagingDataSource.disposable
        set(_) {}

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }
}