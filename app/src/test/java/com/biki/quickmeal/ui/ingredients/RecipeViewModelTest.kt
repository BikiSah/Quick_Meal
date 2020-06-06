package com.biki.quickmeal.ui.ingredients

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.biki.quickmeal.RxImmediateSchedulerRule
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.QuickMealRepository
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.mockPagedList
import com.biki.quickmeal.pagination.datasource.IngredientsPagingDataSource
import com.biki.quickmeal.pagination.datasource.MealsPagingDataSource
import com.biki.quickmeal.pagination.datasource.SearchMealPagingDataSource
import com.biki.quickmeal.pagination.factory.IngredientsPagingDataSourceFactory
import com.biki.quickmeal.pagination.factory.MealsPagingDataSourceFactory
import com.biki.quickmeal.pagination.factory.SearchMealPagingDataSourceFactory
import com.biki.quickmeal.ui.meals.MealsViewModel
import com.biki.quickmeal.ui.recipe.RecipeViewModel
import com.biki.quickmeal.ui.search.SearchMealViewModel
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.*

import org.junit.Assert.*
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RecipeViewModelTest {

    private var viewModel: RecipeViewModel? = null

    @Mock
    lateinit var repository: QuickMealRepository

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private var observerState = mock<Observer<State>>()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = RecipeViewModel(repository)
    }

    @After
    fun close() {
        viewModel = null
    }

    @Test
    fun test_Initialization() {

        assertNotNull(viewModel?.meal)
        assertNotNull(viewModel?.state)

    }

    @Test
    fun test_getRecipeSuccess() {
        val meal = mock<Meal>()
        val observable = Flowable.just(meal)
        val mealId = 1

        whenever(repository.getMealDetails(mealId)).thenReturn(observable)

        viewModel?.state?.observeForever(observerState)

        viewModel?.getRecipe(mealId)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.DONE
        verify(repository).getMealDetails(mealId)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }
    }

    @Test
    fun test_getRecipeError() {
        val errorMessage = "Error response"
        val response = Throwable(errorMessage)
        val mealId = 1

        whenever(repository.getMealDetails(mealId)).thenReturn(Flowable.error(response))

        viewModel?.state?.observeForever(observerState)

        viewModel?.getRecipe(mealId)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.ERROR
        verify(repository).getMealDetails(mealId)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }
    }

    @Test
    fun test_Retry() {
        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        val mealId = 1

        whenever(repository.getMealDetails(mealId)).thenReturn(Flowable.error(response))

        viewModel?.state?.observeForever(observerState)

        viewModel?.getRecipe(mealId)

        viewModel?.retry()
        verify(repository, times(2)).getMealDetails(mealId)

    }

}