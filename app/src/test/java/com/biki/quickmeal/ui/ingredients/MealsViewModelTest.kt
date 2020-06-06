package com.biki.quickmeal.ui.ingredients

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.mockPagedList
import com.biki.quickmeal.pagination.datasource.IngredientsPagingDataSource
import com.biki.quickmeal.pagination.datasource.MealsPagingDataSource
import com.biki.quickmeal.pagination.factory.IngredientsPagingDataSourceFactory
import com.biki.quickmeal.pagination.factory.MealsPagingDataSourceFactory
import com.biki.quickmeal.ui.meals.MealsViewModel
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MealsViewModelTest {

    private var viewModel: MealsViewModel? = null
    @Mock
    lateinit var pagingDataSourceFactory: MealsPagingDataSourceFactory

    @Mock
    lateinit var pagingDataSource: MealsPagingDataSource

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(pagingDataSourceFactory.mealsPagingDataSource).thenReturn(pagingDataSource)
        whenever(pagingDataSourceFactory.mealsPagingDataSource.state).thenReturn(
            MutableLiveData()
        )

        viewModel = MealsViewModel(pagingDataSourceFactory)
    }

    @After
    fun close() {
        viewModel = null
    }

    @Test
    fun test_Initialization() {

        assertNotNull(viewModel?.meals)
        assertNotNull(viewModel?.state)

    }

    @Test
    fun test_Retry() {
        viewModel?.retry()

        verify(pagingDataSourceFactory.mealsPagingDataSource).retry()

    }

    @Test
    fun test_ListIsEmpty() {

        assertTrue(viewModel!!.listIsEmpty())

        val ingredient = mock<Meal>()
        val pagedIngredientsList = MutableLiveData(mockPagedList(listOf(ingredient)))

        viewModel?.meals = pagedIngredientsList

        assertFalse(viewModel!!.listIsEmpty())

    }

    @Test
    fun test_Disposable() {
        viewModel?.disposable

        verify(pagingDataSourceFactory.mealsPagingDataSource).disposable

    }
}