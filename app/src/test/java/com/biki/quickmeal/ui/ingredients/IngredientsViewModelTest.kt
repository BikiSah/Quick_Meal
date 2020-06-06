package com.biki.quickmeal.ui.ingredients

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.mockPagedList
import com.biki.quickmeal.pagination.datasource.IngredientsPagingDataSource
import com.biki.quickmeal.pagination.factory.IngredientsPagingDataSourceFactory
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class IngredientsViewModelTest {

    private var viewModel: IngredientsViewModel? = null
    @Mock
    lateinit var pagingDataSourceFactory: IngredientsPagingDataSourceFactory

    @Mock
    lateinit var pagingDataSource: IngredientsPagingDataSource

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private var observerState = mock<Observer<State>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(pagingDataSourceFactory.ingredientsPagingDataSource).thenReturn(pagingDataSource)
        whenever(pagingDataSourceFactory.ingredientsPagingDataSource.state).thenReturn(
            MutableLiveData()
        )

        viewModel = IngredientsViewModel(pagingDataSourceFactory)
    }

    @After
    fun close() {
        viewModel = null
    }

    @Test
    fun test_Initialization() {

        assertNotNull(viewModel?.ingredients)
        assertNotNull(viewModel?.state)

        /*viewModel.state.observeForever(observerState)
        verify(observerState).onChanged(State.LOADING)*/
    }

    @Test
    fun test_Retry() {
        viewModel?.retry()

        verify(pagingDataSourceFactory.ingredientsPagingDataSource).retry()

    }

    @Test
    fun test_ListIsEmpty() {

        assertTrue(viewModel!!.listIsEmpty())

        val ingredient = mock<Ingredient>()
        val pagedIngredientsList = MutableLiveData(mockPagedList(listOf(ingredient)))

        viewModel?.ingredients = pagedIngredientsList

        assertFalse(viewModel!!.listIsEmpty())

    }

    @Test
    fun test_Disposable() {
        viewModel?.disposable

        verify(pagingDataSourceFactory.ingredientsPagingDataSource).disposable

    }
}