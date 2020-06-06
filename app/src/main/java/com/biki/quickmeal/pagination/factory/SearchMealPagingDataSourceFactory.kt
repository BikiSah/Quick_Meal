package com.biki.quickmeal.pagination.factory

import androidx.paging.DataSource
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.pagination.datasource.SearchMealPagingDataSource
import javax.inject.Inject

class SearchMealPagingDataSourceFactory @Inject constructor(val searchMealPagingDataSource: SearchMealPagingDataSource) :
    DataSource.Factory<Int, Meal>() {

    override fun create(): DataSource<Int, Meal> {
        return searchMealPagingDataSource
    }
}