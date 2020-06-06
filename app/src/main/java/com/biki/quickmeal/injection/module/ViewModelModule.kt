

package com.biki.quickmeal.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.biki.quickmeal.injection.ViewModelFactory
import com.biki.quickmeal.ui.ingredients.IngredientsViewModel
import com.biki.quickmeal.ui.meals.MealsViewModel
import com.biki.quickmeal.ui.recipe.RecipeViewModel
import com.biki.quickmeal.ui.search.SearchMealViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
open class ViewModelModule {

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(IngredientsViewModel::class)
    fun providesIngredientsViewModel(viewModel: IngredientsViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(SearchMealViewModel::class)
    fun providesSearchMealsViewModel(viewModel: SearchMealViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(MealsViewModel::class)
    fun providesMealsViewModel(viewModel: MealsViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(RecipeViewModel::class)
    fun providesRecipeViewModel(viewModel: RecipeViewModel): ViewModel = viewModel


}