package com.biki.quickmeal.pagination.factory

import androidx.paging.DataSource
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.pagination.datasource.MealsPagingDataSource
import javax.inject.Inject

class MealsPagingDataSourceFactory @Inject constructor(val mealsPagingDataSource: MealsPagingDataSource) :
    DataSource.Factory<Int, Meal>() {

    override fun create(): DataSource<Int, Meal> {
        return mealsPagingDataSource
    }
}