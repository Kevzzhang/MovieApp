package com.kevin.movieapp.utils

import com.kevin.base_application.utils.ErrorUtils
import retrofit2.HttpException

open class BaseAPIService : IBaseAPIService {

    override fun <T> storeResult(data: T, methodName: String, tag: String): LiveDataResult<T> {
        return LiveDataResult<T>(Status.SUCCESS, data, "")
    }

    override fun <T> storeError(
        error: Throwable,
        methodName: String,
        tag: String
    ): LiveDataResult<T> {
        var message = ""
        try {
            if (error is HttpException) {
                val errorResponse = ErrorUtils.parseError(error)
                return LiveDataResult(Status.ERROR, errorResponse, errorResponse.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return LiveDataResult(Status.ERROR, e, e.message.toString())
        }

        return LiveDataResult(Status.ERROR, null, message)
    }

}

interface IBaseAPIService {
    fun <T> storeResult(`object`: T, methodName: String, tag: String): LiveDataResult<*>
    fun <T> storeError(error: Throwable, methodName: String, tag: String): LiveDataResult<T>
}