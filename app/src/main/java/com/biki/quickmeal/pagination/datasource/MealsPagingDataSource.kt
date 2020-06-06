package com.biki.quickmeal.pagination.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.QuickMealRepository
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.extensions.addToCompositeDisposable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MealsPagingDataSource @Inject constructor(private val repository: QuickMealRepository) :
    PageKeyedDataSource<Int, Meal>() {
    val disposable = CompositeDisposable()
    //LiveData object for state
    var state = MutableLiveData<State>()
    var ingredient = MutableLiveData<String>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    /**
     * Creating the observable for specific page to call the API
     */
    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    //Retrying the API call
    fun retry() {
        if (retryCompletable != null) {
            retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe().addToCompositeDisposable(disposable)
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Meal>
    ) {
        updateState(State.LOADING)
        val currentPage = 1
        val nextPage = currentPage + 1
        //Call api
        repository.getMeals(ingredient.value!!)
            .subscribe(
                { meals ->
                    updateState(State.DONE)
                    callback.onResult(meals, null, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadInitial(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Meal>) {
        updateState(State.DONE)
        callback.onResult(emptyList(), null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Meal>) {
    }
}