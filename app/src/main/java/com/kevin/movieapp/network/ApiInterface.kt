package com.kevin.movieapp.network

import com.kevin.movieapp.model.movie_discover.MovieDiscoverResp
import com.kevin.movieapp.model.movie_genre.MovieGenreResp
import com.kevin.movieapp.model.movie_review.MovieReviewResp
import com.kevin.movieapp.model.movie_video.MovieVideoResp
import com.kevin.movieapp.model.movie_detail.MovieDetail
import com.kevin.movieapp.utils.AppConstant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET(AppConstant.API_DISCOVER)
    suspend fun getMovieList(
        @Query("sort_by") sortBy: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("include_video") includeVideo: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String
    ): Response<MovieDiscoverResp>

    @GET(AppConstant.API_DETAIL + "/{movie_id}" + AppConstant.API_KEY_AND_LANGUAGE)
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int
    ): Response<MovieDetail>

    @GET(AppConstant.API_GENRE_MOVIE)
    suspend fun getGenre(): Response<MovieGenreResp>

    @GET("movie/{movie_id}/videos${AppConstant.API_KEY_AND_LANGUAGE}")
    suspend fun getMovieVideo(
        @Path("movie_id") id: Int
    ): Response<MovieVideoResp>

    @GET("movie/{movie_id}/reviews${AppConstant.API_KEY_AND_LANGUAGE}")
    suspend fun getMovieReviews(
        @Path("movie_id") id: Int,
        @Query("page") page: Int
    ): Response<MovieReviewResp>
}