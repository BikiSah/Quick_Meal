package com.biki.quickmeal.pagination.factory

import androidx.paging.DataSource
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.pagination.datasource.IngredientsPagingDataSource
import javax.inject.Inject

class IngredientsPagingDataSourceFactory @Inject constructor(val ingredientsPagingDataSource: IngredientsPagingDataSource) :
    DataSource.Factory<Int, Ingredient>() {

    override fun create(): DataSource<Int, Ingredient> {
        return ingredientsPagingDataSource
    }
}