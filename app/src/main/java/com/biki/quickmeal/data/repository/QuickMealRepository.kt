
package com.biki.quickmeal.data.repository

import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import com.biki.quickmeal.injection.annotations.Local
import com.biki.quickmeal.injection.annotations.Remote
import io.reactivex.Flowable
import javax.inject.Inject

/**
 *
 * @author biki Sah.
 */
class QuickMealRepository @Inject constructor(@Local private val localDataSource: QuickMealDataSource, @Remote private val remoteDataSource: QuickMealDataSource) :
    QuickMealDataSource {
    override fun getIngredients(): Flowable<List<Ingredient>> = remoteDataSource.getIngredients()

    override fun getMeals(ingredient: String): Flowable<List<Meal>> =
        remoteDataSource.getMeals(ingredient)

    override fun getMealDetails(mealId: Int): Flowable<Meal> =
        remoteDataSource.getMealDetails(mealId)

    override fun searchMeals(mealName: String): Flowable<List<Meal>> =
        remoteDataSource.searchMeals(mealName)

}