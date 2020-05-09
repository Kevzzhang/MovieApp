package com.kevin.movieapp.repo

import com.kevin.movieapp.network.APIClient
import com.kevin.movieapp.network.ApiInterface
import com.kevin.movieapp.utils.BaseAPIService
import com.kevin.movieapp.utils.LiveDataResult
import retrofit2.HttpException

interface MovieReviewInterface {
    suspend fun getMovieReviews(id: Int, page: Int): LiveDataResult<Any?>
}

class MovieReviewRepo : MovieReviewInterface, BaseAPIService() {
    companion object {
        private val TAG = MovieReviewRepo::class.java.simpleName
    }

    private val apiClient = APIClient.client.create(ApiInterface::class.java)

    override suspend fun getMovieReviews(id: Int, page: Int): LiveDataResult<Any?> {
        try {
            val result = apiClient.getMovieReviews(id, page)

            if (result.isSuccessful) {
                val response = result.body()
                val data: LiveDataResult<Any?> = storeResult(response, "", TAG)
                return data
            } else {
                val exception = HttpException(result)
                return storeError(exception, "", TAG)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return storeError(e, "", TAG)
        }
    }
}
