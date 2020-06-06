

package com.biki.quickmeal.data.repository.remote


import com.biki.quickmeal.data.api.QuickMealService
import com.biki.quickmeal.data.repository.QuickMealDataSource
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.data.repository.remote.model.Meal
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle remote operations
 *
 */
class QuickMealRemoteDataSource @Inject constructor(private var remoteService: QuickMealService) :
    QuickMealDataSource {
    override fun getIngredients(): Flowable<List<Ingredient>> =
        remoteService.fetchIngredients().map {
            it.ingredients
        }.toFlowable().take(1)

    override fun getMeals(ingredient: String): Flowable<List<Meal>> =
        remoteService.fetchMeals(ingredient).map {
            it.meals
        }.toFlowable().take(1)

    override fun getMealDetails(mealId: Int): Flowable<Meal> =
        remoteService.fetchMealDetails(mealId).map {
            it.meals[0]
        }.toFlowable().take(1)

    override fun searchMeals(mealName: String): Flowable<List<Meal>> =
        remoteService.searchMeal(mealName).map {
            it.meals
        }.toFlowable().take(1)

}