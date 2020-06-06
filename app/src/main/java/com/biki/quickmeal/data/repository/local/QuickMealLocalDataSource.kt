

package com.biki.quickmeal.data.repository.local

import com.biki.quickmeal.data.repository.QuickMealDataSource
import com.biki.quickmeal.data.repository.remote.model.Meal
import com.biki.quickmeal.data.repository.remote.model.Ingredient
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle local db operations
 *
 */
class QuickMealLocalDataSource @Inject constructor() : QuickMealDataSource {
    override fun getIngredients(): Flowable<List<Ingredient>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMeals(ingredient: String): Flowable<List<Meal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMealDetails(mealId: Int): Flowable<Meal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchMeals(mealName: String): Flowable<List<Meal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}