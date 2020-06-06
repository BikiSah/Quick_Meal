

package com.biki.quickmeal.data.repository

import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import io.reactivex.Flowable

/**
 * @author Sanjay Sah
 */
interface QuickMealDataSource {

    fun getIngredients(): Flowable<List<Ingredient>>

    fun getMeals(ingredient: String): Flowable<List<Meal>>

    fun getMealDetails(mealId: Int): Flowable<Meal>

    fun searchMeals(mealName: String): Flowable<List<Meal>>
}