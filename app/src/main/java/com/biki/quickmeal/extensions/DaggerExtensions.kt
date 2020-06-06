

package com.biki.quickmeal.extensions
import android.content.Context
import com.biki.quickmeal.QuickMealApplication


val Context.appComponent
    get() = (applicationContext as QuickMealApplication).appComponent
