package com.kevin.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.movieapp.R
import com.kevin.movieapp.model.ErrorResponse
import com.kevin.movieapp.model.movie_video.MovieVideo
import com.kevin.movieapp.model.movie_video.MovieVideoResp
import com.kevin.movieapp.model.movie_detail.MovieDetail
import com.kevin.movieapp.repo.MovieDetailRepo
import com.kevin.movieapp.utils.LiveDataResult
import com.kevin.movieapp.utils.SingleLiveEvent
import com.kevin.movieapp.utils.Status
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private var movieId: Int = 0
    private var movieDetail: MovieDetail? = null
    private var movieVideo: MovieVideo ?= null

    private var repo : MovieDetailRepo?=null

    var movieDetailResp: SingleLiveEvent<MovieDetail?>
    var movieVideoResp: SingleLiveEvent<MovieVideo>

    var errorResp: SingleLiveEvent<Any?>
    var showLoading: SingleLiveEvent<Boolean?>
    var showErrorLayout: SingleLiveEvent<Boolean?>

    private var isMovieDetailSuccess = false
    private var isMovieVideoSuccess = false

    init {
        movieDetailResp = SingleLiveEvent()
        movieVideoResp = SingleLiveEvent()

        errorResp = SingleLiveEvent()
        showLoading = SingleLiveEvent()
        showErrorLayout = SingleLiveEvent()
    }

    fun initCheckLocalData() {
        if (movieDetail!=null) movieDetailResp.value = movieDetail else fetchMovieDetail()

        if(movieVideo!=null) movieVideoResp.value = movieVideo else fetchMovieVideo()
    }

    fun getMovieId():Int{
        return movieId
    }

    fun setupRepo(repo : MovieDetailRepo){
        this.repo = repo
    }

    private fun updateMovieDetailSuccess(isSuccess : Boolean){
        isMovieDetailSuccess = isSuccess
    }

    private fun updateMovieVideoSuccess(isSuccess : Boolean){
        isMovieVideoSuccess = isSuccess
    }

    fun getIsMovieDetailSuccess() : Boolean = isMovieDetailSuccess
    fun getIsMovieVideoSuccess() : Boolean = isMovieVideoSuccess

    fun updateMovieId(id : Int){
        movieId = id
    }

    private fun showLoading(show: Boolean) {
        showLoading.value = show
    }

    private fun showErrorLayout(show: Boolean) {
        showErrorLayout.value = show
    }

    fun fetchMovieDetail() {
        showLoading(true)

        viewModelScope.launch {
            val resp = repo?.getMovieDetail(movieId)
            showLoading(false)

            if (resp?.status == Status.SUCCESS) {
                handleSuccessFetchMovieDetail(resp)
            } else {
                updateMovieDetailSuccess(false)
                handleErrorResponse(resp)
            }
        }
    }

    fun fetchMovieVideo() {
        showLoading(true)

        viewModelScope.launch {
            val resp = repo?.getMovieVideo(movieId)
            showLoading(false)

            if (resp?.status == Status.SUCCESS) {
                handleSuccessFetchMovieVideo(resp)
            } else {
                updateMovieVideoSuccess(false)
                handleErrorResponse(resp)
            }
        }
    }

    private fun handleSuccessFetchMovieDetail(resp: LiveDataResult<Any?>) {
        if (resp.data != null) {
            try {
                val data = resp.data as MovieDetail
                movieDetailResp.value = data
                movieDetail = data
                updateMovieVideoSuccess(true)

            } catch (e: Exception) {
                updateMovieDetailSuccess(false)
                errorResp.value = e.message
            }
        } else {
            updateMovieDetailSuccess(false)
            errorResp.value = R.string.error_api_message
        }
    }

    private fun handleSuccessFetchMovieVideo(resp: LiveDataResult<Any?>) {
        if (resp.data != null) {
            try {
                val data = resp.data as MovieVideoResp
                if (data.results.size > 0) {
                    val lastVideo = data.results.size - 1
                    movieVideoResp.value = data.results[lastVideo]
                    movieVideo = data.results[lastVideo]
                } else {
                    errorResp.value = R.string.no_video
                }
                updateMovieVideoSuccess(true)

            } catch (e: Exception) {
                updateMovieVideoSuccess(false)
                errorResp.value = e.message
            }
        } else {
            updateMovieVideoSuccess(false)
            errorResp.value = R.string.error_api_message
        }
    }

    private fun handleErrorResponse(resp: LiveDataResult<Any?>?) {
        showErrorLayout(true)
        try {
            val error = resp?.data as ErrorResponse
            if (!error.message.isNullOrEmpty()) {
                errorResp.value = error.message
            } else {
                errorResp.value = R.string.error_api_message
            }
        } catch (e: Exception) {
            errorResp.value = e.message
        }
    }
}