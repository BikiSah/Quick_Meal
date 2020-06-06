package com.biki.quickmeal.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.pagination.factory.SearchMealPagingDataSourceFactory
import com.biki.quickmeal.ui.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SearchMealViewModel @Inject constructor(private val pagingDataSourceFactory: SearchMealPagingDataSourceFactory) :
    BaseViewModel() {
    //LiveData object for Meals
    var meals: LiveData<PagedList<Meal>>? = null
    //LiveData object for state
    var state = pagingDataSourceFactory.searchMealPagingDataSource.state
    var searchQuery = pagingDataSourceFactory.searchMealPagingDataSource.searchQuery

    init {
        //Setting up Paging for fetching the meals in pagination
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        meals = Transformations.switchMap(searchQuery) { _ ->
            LivePagedListBuilder<Int, Meal>(
                pagingDataSourceFactory, config
            ).build()
        }
    }

    fun listIsEmpty(): Boolean {
        return meals?.value?.isEmpty() ?: true
    }

    //Retrying the API call
    fun retry() {
        pagingDataSourceFactory.searchMealPagingDataSource.retry()
    }

    override var disposable: CompositeDisposable
        get() = pagingDataSourceFactory.searchMealPagingDataSource.disposable
        set(_) {}

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }
}