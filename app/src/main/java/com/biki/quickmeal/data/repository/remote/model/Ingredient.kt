package com.biki.quickmeal.data.repository.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class IngredientsResponse(
    @SerializedName("meals") val ingredients: List<Ingredient>

)

@Parcelize
data class Ingredient(
    @SerializedName("idIngredient") val id: String,
    @SerializedName("strIngredient") val name: String

) : Parcelable