package com.biki.quickmeal.pagination.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.QuickMealRepository
import com.biki.quickmeal.RxImmediateSchedulerRule
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import io.reactivex.Flowable
import org.junit.*
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class IngredientsPagingDataSourceTest {

    private var pagingDataSource: IngredientsPagingDataSource? = null

    @Mock
    lateinit var repository: QuickMealRepository

    private var loadInitialParams = PageKeyedDataSource.LoadInitialParams<Int>(5, false)

    @Mock
    lateinit var loadInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, Ingredient>

    private var loadParams = PageKeyedDataSource.LoadParams(1, 20)

    @Mock
    lateinit var loadCallback: PageKeyedDataSource.LoadCallback<Int, Ingredient>

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private var observerState = mock<Observer<State>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pagingDataSource = IngredientsPagingDataSource(repository)
    }

    @After
    fun tearDown() {
        pagingDataSource = null
    }

    @Test
    fun loadInitial_Success() {
        val ingredientsList = emptyList<Ingredient>()
        val observable = Flowable.just(ingredientsList)

        whenever(repository.getIngredients()).thenReturn(
            observable
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.DONE

        verify(repository).getIngredients()
        verify(loadInitialCallback).onResult(ingredientsList, null, 2)
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }
    }

    @Test
    fun loadInitial_Error() {
        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(repository.getIngredients()).thenReturn(
            Flowable.error(response)
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.ERROR

        verify(repository).getIngredients()

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }

    }

    @Test
    fun loadInitial_Retry() {

        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.getIngredients()
        ).thenReturn(Flowable.error(response))

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        pagingDataSource!!.retry()

        verify(repository, times(2)).getIngredients()
    }

    @Test
    fun loadAfter() {
        pagingDataSource!!.state.observeForever(observerState)
        pagingDataSource!!.loadAfter(loadParams, loadCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedDoneState = State.DONE
        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (doneState) = allValues
            assertEquals(doneState, expectedDoneState)
        }

        verify(loadCallback).onResult(emptyList(), null)
    }
}