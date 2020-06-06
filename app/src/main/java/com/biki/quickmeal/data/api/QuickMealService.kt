

package com.biki.quickmeal.data.api

import com.biki.quickmeal.data.repository.remote.model.IngredientsResponse
import com.biki.quickmeal.data.repository.remote.model.MealsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**Interface where all the api used in app are defined.
 */
interface QuickMealService {

    /**
     * Api for fetching ingredients list
     */
    @GET("v1/1/list.php?i=list")
    fun fetchIngredients(): Single<IngredientsResponse>

    /**
     * Api for fetching meals filter by main ingredients
     */
    @GET("v1/1/filter.php")
    fun fetchMeals(@Query("i") ingredient: String): Single<MealsResponse>

    /**
     * Api for fetching meal details
     */
    @GET("v1/1/lookup.php")
    fun fetchMealDetails(@Query("i") mealId: Int): Single<MealsResponse>

    /**
     * Api for searching meals
     */
    @GET("v1/1/search.php")
    fun searchMeal(@Query("s") query: String): Single<MealsResponse>


}