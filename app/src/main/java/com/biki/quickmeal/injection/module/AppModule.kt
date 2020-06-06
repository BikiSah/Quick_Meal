/*
 * AppModule.kt
 * Created by Sanjay.Sah
 */

package com.biki.quickmeal.injection.module


import android.content.Context
import com.biki.quickmeal.QuickMealApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
open class AppModule(private val application: QuickMealApplication) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): QuickMealApplication = application

}