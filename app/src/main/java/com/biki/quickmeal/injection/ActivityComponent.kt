

package com.biki.quickmeal.injection

import com.biki.quickmeal.injection.annotations.PerActivity
import com.biki.quickmeal.injection.module.ActivityModule
import com.biki.quickmeal.ui.ingredients.IngredientsActivity
import com.biki.quickmeal.ui.meals.MealsActivity
import com.biki.quickmeal.ui.recipe.RecipeActivity
import com.biki.quickmeal.ui.search.SearchMealsActivity
import dagger.Subcomponent



@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: IngredientsActivity)
    fun inject(activity: SearchMealsActivity)
    fun inject(activity: MealsActivity)
    fun inject(activity: RecipeActivity)

}