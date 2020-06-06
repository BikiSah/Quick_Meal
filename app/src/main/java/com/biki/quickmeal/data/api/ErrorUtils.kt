

package com.biki.quickmeal.data.api

import com.google.gson.Gson
import com.biki.quickmeal.data.repository.remote.model.APIError
import retrofit2.Response

/**
 * Class used to parse the error response received in api calling
 */
object ErrorUtils {

    fun<T> parseError(response: Response<T>): APIError {

        return Gson().fromJson(response.errorBody()!!.charStream(), APIError::class.java)
    }
}