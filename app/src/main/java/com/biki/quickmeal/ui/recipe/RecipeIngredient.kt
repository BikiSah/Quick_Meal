package com.biki.quickmeal.ui.recipe

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeIngredient(val name: String, val quantity: String): Parcelable