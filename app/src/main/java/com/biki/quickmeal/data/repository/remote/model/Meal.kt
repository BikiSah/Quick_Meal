

package com.biki.quickmeal.data.repository.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.biki.quickmeal.ui.recipe.RecipeIngredient
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


data class MealsResponse(
    val meals: List<Meal>

)

@Parcelize
data class Meal(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumbnail: String,

    @SerializedName("strCategory") val category: String?,
    @SerializedName("strArea") val area: String?,
    @SerializedName("strInstructions") val instructions: String?,
    @SerializedName("strYoutube") val youtubeLink: String?,

    @SerializedName("strIngredient1") val ingredient1: String?,
    @SerializedName("strIngredient2") val ingredient2: String?,
    @SerializedName("strIngredient3") val ingredient3: String?,
    @SerializedName("strIngredient4") val ingredient4: String?,
    @SerializedName("strIngredient5") val ingredient5: String?,
    @SerializedName("strIngredient6") val ingredient6: String?,
    @SerializedName("strIngredient7") val ingredient7: String?,
    @SerializedName("strIngredient8") val ingredient8: String?,
    @SerializedName("strIngredient9") val ingredient9: String?,
    @SerializedName("strIngredient10") val ingredient10: String?,
    @SerializedName("strIngredient11") val ingredient11: String?,
    @SerializedName("strIngredient12") val ingredient12: String?,
    @SerializedName("strIngredient13") val ingredient13: String?,
    @SerializedName("strIngredient14") val ingredient14: String?,
    @SerializedName("strIngredient15") val ingredient15: String?,
    @SerializedName("strIngredient16") val ingredient16: String?,
    @SerializedName("strIngredient17") val ingredient17: String?,
    @SerializedName("strIngredient18") val ingredient18: String?,
    @SerializedName("strIngredient19") val ingredient19: String?,
    @SerializedName("strIngredient20") val ingredient20: String?,

    @SerializedName("strMeasure1") val ingredient1Quantity: String?,
    @SerializedName("strMeasure2") val ingredient2Quantity: String?,
    @SerializedName("strMeasure3") val ingredient3Quantity: String?,
    @SerializedName("strMeasure4") val ingredient4Quantity: String?,
    @SerializedName("strMeasure5") val ingredient5Quantity: String?,
    @SerializedName("strMeasure6") val ingredient6Quantity: String?,
    @SerializedName("strMeasure7") val ingredient7Quantity: String?,
    @SerializedName("strMeasure8") val ingredient8Quantity: String?,
    @SerializedName("strMeasure9") val ingredient9Quantity: String?,
    @SerializedName("strMeasure10") val ingredient10Quantity: String?,
    @SerializedName("strMeasure11") val ingredient11Quantity: String?,
    @SerializedName("strMeasure12") val ingredient12Quantity: String?,
    @SerializedName("strMeasure13") val ingredient13Quantity: String?,
    @SerializedName("strMeasure14") val ingredient14Quantity: String?,
    @SerializedName("strMeasure15") val ingredient15Quantity: String?,
    @SerializedName("strMeasure16") val ingredient16Quantity: String?,
    @SerializedName("strMeasure17") val ingredient17Quantity: String?,
    @SerializedName("strMeasure18") val ingredient18Quantity: String?,
    @SerializedName("strMeasure19") val ingredient19Quantity: String?,
    @SerializedName("strMeasure20") val ingredient20Quantity: String?
) : Parcelable {
    @IgnoredOnParcel
    @Transient var recipeIngredients: List<RecipeIngredient>? = null
}
