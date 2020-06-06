package com.biki.quickmeal.ui.ingredients

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.pagination.factory.IngredientsPagingDataSourceFactory
import com.biki.quickmeal.ui.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class IngredientsViewModel @Inject constructor(private val pagingDataSourceFactory: IngredientsPagingDataSourceFactory) :
    BaseViewModel() {
    //LiveData object for ingredients
    var ingredients: LiveData<PagedList<Ingredient>>? = null
    //LiveData object for state
    var state = pagingDataSourceFactory.ingredientsPagingDataSource.state

    init {
        //Setting up Paging for fetching the ingredients in pagination
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        ingredients = LivePagedListBuilder<Int, Ingredient>(
            pagingDataSourceFactory, config
        ).build()
    }

    fun listIsEmpty(): Boolean {
        return ingredients?.value?.isEmpty() ?: true
    }

    //Retrying the API call
    fun retry() {
        pagingDataSourceFactory.ingredientsPagingDataSource.retry()
    }

    override var disposable: CompositeDisposable
        get() = pagingDataSourceFactory.ingredientsPagingDataSource.disposable
        set(_) {}

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }
}