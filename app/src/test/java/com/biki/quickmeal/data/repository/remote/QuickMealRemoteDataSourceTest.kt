package com.biki.quickmeal.data.repository.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.biki.quickmeal.data.api.QuickMealService
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.data.repository.remote.model.IngredientsResponse
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.data.repository.remote.model.MealsResponse
import io.reactivex.Single
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class QuickMealRemoteDataSourceTest {

    @Mock
    lateinit var apiService: QuickMealService

    private var remoteDataSource: QuickMealRemoteDataSource? = null

    private val currentPage = 1
    private val limit = 20

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remoteDataSource = QuickMealRemoteDataSource(apiService)
    }

    @After
    fun tearDown() {
        remoteDataSource = null
    }

    @Test
    fun getIngredients() {
        val ingredientsList = emptyList<Ingredient>()
        val ingredientsResponse = IngredientsResponse(ingredientsList)
        val observable = Single.just(ingredientsResponse)

        whenever(apiService.fetchIngredients()).thenReturn(observable)

        remoteDataSource?.getIngredients()

        verify(apiService).fetchIngredients()

        assertThat(
            apiService.fetchIngredients(),
            instanceOf(Single::class.java)
        )
    }

    @Test
    fun getMeals() {

        val mealsList = emptyList<Meal>()
        val mealsResponse = MealsResponse(mealsList)
        val observable = Single.just(mealsResponse)
        val ingredient = "Chicken"
        val argumentCaptor = argumentCaptor<String>()

        whenever(apiService.fetchMeals(ingredient)).thenReturn(observable)

        remoteDataSource?.getMeals(ingredient)

        verify(apiService).fetchMeals(
            argumentCaptor.capture()
        )

        assertThat(
            apiService.fetchMeals(argumentCaptor.firstValue),
            instanceOf(Single::class.java)
        )
    }

    @Test
    fun getMealDetails() {
        val mealsList = emptyList<Meal>()
        val mealsResponse = MealsResponse(mealsList)
        val observable = Single.just(mealsResponse)
        val mealId = 1
        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)

        whenever(apiService.fetchMealDetails(mealId)).thenReturn(observable)

        remoteDataSource?.getMealDetails(mealId)

        verify(apiService).fetchMealDetails(
            argumentCaptor.capture()
        )

        assertThat(
            apiService.fetchMealDetails(mealId),
            instanceOf(Single::class.java)
        )
    }

    @Test
    fun searchMeals() {
        val mealsList = emptyList<Meal>()
        val mealsResponse = MealsResponse(mealsList)
        val observable = Single.just(mealsResponse)
        val searchQuery = "Chicken"
        val argumentCaptor = argumentCaptor<String>()

        whenever(apiService.searchMeal(searchQuery)).thenReturn(observable)

        remoteDataSource?.searchMeals(searchQuery)

        verify(apiService).searchMeal(
            argumentCaptor.capture()
        )

        assertThat(
            apiService.searchMeal(argumentCaptor.firstValue),
            instanceOf(Single::class.java)
        )
    }
}