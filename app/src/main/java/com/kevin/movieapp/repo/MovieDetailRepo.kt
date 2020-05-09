package com.kevin.movieapp.repo

import com.kevin.movieapp.network.APIClient
import com.kevin.movieapp.network.ApiInterface
import com.kevin.movieapp.utils.BaseAPIService
import com.kevin.movieapp.utils.LiveDataResult
import retrofit2.HttpException

interface MovieDetailInterface {
    suspend fun getMovieDetail(id: Int): LiveDataResult<Any?>
    suspend fun getMovieVideo(id : Int): LiveDataResult<Any?>
}

class MovieDetailRepo() : MovieDetailInterface, BaseAPIService() {
    companion object{
        private val TAG = MovieDetailRepo::class.java.simpleName
    }
    private val apiClient = APIClient.client.create(ApiInterface::class.java)

    override suspend fun getMovieDetail(id: Int): LiveDataResult<Any?> {
        try {
            val result = apiClient.getMovieDetail(id)

            if (result.isSuccessful) {
                val response = result.body()
                val data: LiveDataResult<Any?> = storeResult(response, "", TAG)
                return data
            } else {
                val exception = HttpException(result)
                val data: LiveDataResult<Any?> = storeError(exception, "", TAG)
                return data
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val data: LiveDataResult<Any?> = storeError(e, "", TAG)
            return data
        }
    }

    override suspend fun getMovieVideo(id: Int): LiveDataResult<Any?> {
        try {
            val result = apiClient.getMovieVideo(id)

            if (result.isSuccessful) {
                val response = result.body()
                val data: LiveDataResult<Any?> = storeResult(response, "", TAG)
                return data
            } else {
                val exception = HttpException(result)
                val data: LiveDataResult<Any?> = storeError(exception, "", TAG)
                return data
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val data: LiveDataResult<Any?> = storeError(e, "", TAG)
            return data
        }
    }

}