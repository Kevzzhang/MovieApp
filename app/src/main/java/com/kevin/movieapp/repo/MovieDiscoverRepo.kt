package com.kevin.movieapp.repo

import com.kevin.movieapp.network.APIClient
import com.kevin.movieapp.network.ApiInterface
import com.kevin.movieapp.utils.BaseAPIService
import com.kevin.movieapp.utils.LiveDataResult
import retrofit2.HttpException

interface MovieInterface {
    suspend fun getMovieList(genre: String, page: Int): LiveDataResult<Any?>
    suspend fun getGenreList(): LiveDataResult<Any?>
}

class MovieDiscoverRepo() : MovieInterface, BaseAPIService() {

    companion object{
        private val TAG = MovieDiscoverRepo::class.java.simpleName
    }
    private val apiClient = APIClient.client.create(ApiInterface::class.java)

    override suspend fun getMovieList(genre: String, page: Int): LiveDataResult<Any?> {
        try {
            val result = apiClient.getMovieList(
                "popularity.desc", false,
                false, page,
                genre
            )

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

    override suspend fun getGenreList(): LiveDataResult<Any?> {
        try {
            val result = apiClient.getGenre()

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