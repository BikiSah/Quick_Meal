package com.biki.quickmeal.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.biki.quickmeal.data.repository.local.QuickMealLocalDataSource
import com.biki.quickmeal.data.repository.remote.QuickMealRemoteDataSource
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.data.repository.remote.model.Meal
import io.reactivex.Flowable
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class QuickMealRepositoryTest {

    @Mock
    lateinit var localRepository: QuickMealLocalDataSource
    @Mock
    lateinit var remoteRepository: QuickMealRemoteDataSource

    private var repository: QuickMealRepository? = null

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = QuickMealRepository(localRepository, remoteRepository)
    }

    @After
    fun tearDown() {
        repository = null
    }


    @Test
    fun getIngredients() {
        val ingredientsList = emptyList<Ingredient>()
        val observable = Flowable.just(ingredientsList)

        whenever(remoteRepository.getIngredients()).thenReturn(observable)

        repository?.getIngredients()

        verify(remoteRepository).getIngredients()

        assertThat(
            remoteRepository.getIngredients(),
            instanceOf(Flowable::class.java)
        )
    }

    @Test
    fun getMeals() {

        val mealsList = emptyList<Meal>()
        val observable = Flowable.just(mealsList)
        val ingredient = "Chicken"

        val argumentCaptor = argumentCaptor<String>()

        whenever(remoteRepository.getMeals(ingredient)).thenReturn(observable)

        repository?.getMeals(ingredient)

        verify(remoteRepository).getMeals(
            argumentCaptor.capture()
        )

        assertThat(
            remoteRepository.getMeals(argumentCaptor.firstValue),
            instanceOf(Flowable::class.java)
        )
    }

    @Test
    fun getMealDetails() {
        val meal = mock<Meal>()
        val observable = Flowable.just(meal)
        val mealId = 1
        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)

        whenever(remoteRepository.getMealDetails(mealId)).thenReturn(observable)

        repository?.getMealDetails(mealId)

        verify(remoteRepository).getMealDetails(
            argumentCaptor.capture()
        )

        assertThat(
            remoteRepository.getMealDetails(mealId),
            instanceOf(Flowable::class.java)
        )
    }

    @Test
    fun searchMeals() {
        val mealsList = emptyList<Meal>()
        val observable = Flowable.just(mealsList)
        val searchQuery = "Chicken"
        val argumentCaptor = argumentCaptor<String>()

        whenever(remoteRepository.searchMeals(searchQuery)).thenReturn(observable)

        repository?.searchMeals(searchQuery)

        verify(remoteRepository).searchMeals(
            argumentCaptor.capture()
        )

        assertThat(
            remoteRepository.searchMeals(argumentCaptor.firstValue),
            instanceOf(Flowable::class.java)
        )
    }
}