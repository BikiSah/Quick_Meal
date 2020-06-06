package com.biki.quickmeal.ui.recipe

import androidx.lifecycle.MutableLiveData
import com.biki.quickmeal.constants.State
import com.biki.quickmeal.data.repository.QuickMealRepository
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.extensions.addToCompositeDisposable
import com.biki.quickmeal.ui.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeViewModel @Inject constructor(private val repository: QuickMealRepository) :
    BaseViewModel() {
    //LiveData object for Meals
    var meal = MutableLiveData<Meal>()
    //LiveData object for state
    var state = MutableLiveData<State>()
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

    /**
     * Updating UI state
     */
    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun getRecipe(mealId: Int) {
        updateState(State.LOADING)
        //Call api
        repository.getMealDetails(mealId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                createIngredientsList(it)
            }
            .subscribe(
                { meal ->
                    updateState(State.DONE)
                    this.meal?.value = meal

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { getRecipe(mealId) })
                }
            ).addToCompositeDisposable(disposable)
    }

    /**
     * Creating a list of recipe ingredients from all the recipe ingredient parameters. This list would be displayed using
     * RecyclerView in the RecipeIngredientsFragment screen.
     */
    private fun createIngredientsList(meal: Meal): Meal {
        val recipeIngredients = arrayListOf<RecipeIngredient>()
        try {
            with(meal) {
                if (ingredient1!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient1, ingredient1Quantity!!))
                }
                if (ingredient2!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient2, ingredient2Quantity!!))
                }
                if (ingredient3!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient3, ingredient3Quantity!!))
                }
                if (ingredient4!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient4, ingredient4Quantity!!))
                }
                if (ingredient5!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient5, ingredient5Quantity!!))
                }
                if (ingredient6!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient6, ingredient6Quantity!!))
                }
                if (ingredient7!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient7, ingredient7Quantity!!))
                }
                if (ingredient8!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient8, ingredient8Quantity!!))
                }
                if (ingredient9!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient9, ingredient9Quantity!!))
                }
                if (ingredient10!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient10, ingredient10Quantity!!))
                }
                if (ingredient11!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient11, ingredient11Quantity!!))
                }
                if (ingredient12!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient12, ingredient12Quantity!!))
                }
                if (ingredient13!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient13, ingredient13Quantity!!))
                }
                if (ingredient14!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient14, ingredient14Quantity!!))
                }
                if (ingredient15!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient15, ingredient15Quantity!!))
                }
                if (ingredient16!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient16, ingredient16Quantity!!))
                }
                if (ingredient17!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient17, ingredient17Quantity!!))
                }
                if (ingredient18!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient18, ingredient18Quantity!!))
                }
                if (ingredient19!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient19, ingredient19Quantity!!))
                }
                if (ingredient20!!.isNotEmpty()) {
                    recipeIngredients.add(RecipeIngredient(ingredient20, ingredient20Quantity!!))
                }
            }
        } catch (e: Exception) {
        }
        meal.recipeIngredients = recipeIngredients
        return meal
    }

    override var disposable: CompositeDisposable
        get() = CompositeDisposable()
        set(_) {}

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }
}