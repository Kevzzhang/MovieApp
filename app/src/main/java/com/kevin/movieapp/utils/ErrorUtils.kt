package com.kevin.base_application.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kevin.movieapp.model.ErrorResponse
import retrofit2.HttpException


class ErrorUtils {
    companion object {
        fun parseError(exception: HttpException): ErrorResponse? {
            try {
                val errorBody = exception.response()?.errorBody()
                if (errorBody != null) {
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    return Gson().fromJson(errorBody.charStream(), type)
                }

            } catch (e: Exception) {
                return ErrorResponse(500,"Unable to parse JSON.")
            }

            return ErrorResponse(400,"")
        }
    }

}