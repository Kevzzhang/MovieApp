package com.kevin.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.movieapp.R
import com.kevin.movieapp.model.ErrorResponse
import com.kevin.movieapp.model.movie_review.MovieReview
import com.kevin.movieapp.model.movie_review.MovieReviewResp
import com.kevin.movieapp.repo.MovieReviewRepo
import com.kevin.movieapp.utils.LiveDataResult
import com.kevin.movieapp.utils.SingleLiveEvent
import com.kevin.movieapp.utils.Status
import kotlinx.coroutines.launch

class MovieReviewViewModel : ViewModel() {

    private lateinit var repo: MovieReviewRepo
    private var page = 1
    private var movieId = 0
    private var reviews: MutableList<MovieReview>
    private var isHasMore = true

    var movieReviewLiveData: SingleLiveEvent<List<MovieReview?>>
    var movieReviewUpdateLiveData: SingleLiveEvent<List<MovieReview?>>
    var errorResp: SingleLiveEvent<Any?>
    var showLoading: SingleLiveEvent<Boolean?>
    var showErrorLayout: SingleLiveEvent<Boolean?>
    var showEmptyReviewPage : SingleLiveEvent<Boolean?>

    init {
        reviews = arrayListOf()
        movieReviewLiveData = SingleLiveEvent()
        movieReviewUpdateLiveData = SingleLiveEvent()

        errorResp = SingleLiveEvent()
        showLoading = SingleLiveEvent()
        showErrorLayout = SingleLiveEvent()
        showEmptyReviewPage = SingleLiveEvent()
    }

    fun setupRepo(repo: MovieReviewRepo) {
        this.repo = repo
    }

    fun updateMovieId(newId: Int?) {
        if (newId != null) movieId = newId else movieId = 0
    }

    fun getIsHasMoreData(): Boolean {
        return isHasMore
    }

    fun initCheckReviewsLocalData() {
        if (reviews.size > 0) {
            movieReviewLiveData.value = reviews
        } else {
            fetchMovieReview()
        }
    }

    private fun handleShowErrorLayout() {
        showErrorLayout.value = page == 1
    }

    private fun dismissLoading() {
        if (showLoading.value == true)
            showLoading.value = false
    }

    fun handleProgressBar() {
        if (page == 1) {
            showLoading.value = true
        }
    }

    fun fetchMovieReview() {
        if (isHasMore) {
            handleProgressBar()
            viewModelScope.launch {
                val resp = repo.getMovieReviews(movieId, page)
                dismissLoading()
                if (resp.status == Status.SUCCESS) {
                    handleSuccessFetchMovieReviews(resp)
                } else {
                    handleErrorResponse(resp)
                }
            }
        } else {
            errorResp.value = R.string.no_more_data
        }

    }

    private fun handleEmptyReviewPage(data : List<MovieReview>){
        showEmptyReviewPage.value = data.isEmpty()
    }

    private fun handleMovieReviewsData(data: List<MovieReview>?) {
        if(data != null){
            if (page == 1) {
                handleEmptyReviewPage(data)
                movieReviewLiveData.value = data
            } else movieReviewUpdateLiveData.value = data

            this.reviews.addAll(data)
        }

    }

    private fun handleSuccessFetchMovieReviews(resp: LiveDataResult<Any?>) {
        if (resp.data != null) {
            try {
                val data = resp.data as MovieReviewResp
                isHasMore = data.results.isNotEmpty()
                handleMovieReviewsData(data.results)
                page++

            } catch (e: Exception) {
                errorResp.value = e.message
            }
        } else {
            errorResp.value = R.string.error_api_message
        }
    }

    private fun handleErrorResponse(resp: LiveDataResult<Any?>?) {
        handleShowErrorLayout()
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