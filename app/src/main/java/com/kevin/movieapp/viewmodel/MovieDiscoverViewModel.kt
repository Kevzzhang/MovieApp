package com.kevin.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.movieapp.R
import com.kevin.movieapp.model.ErrorResponse
import com.kevin.movieapp.model.movie_discover.MovieDiscover
import com.kevin.movieapp.model.movie_discover.MovieDiscoverResp
import com.kevin.movieapp.model.movie_genre.MovieGenre
import com.kevin.movieapp.model.movie_genre.MovieGenreResp
import com.kevin.movieapp.repo.MovieDiscoverRepo
import com.kevin.movieapp.utils.LiveDataResult
import com.kevin.movieapp.utils.SingleLiveEvent
import com.kevin.movieapp.utils.Status
import kotlinx.coroutines.launch

class MovieDiscoverViewModel() : ViewModel() {

    private var page = 1
    private var genres : MutableList<MovieGenre>
    private var movies : MutableList<MovieDiscover>

    private var genre = ""
    private var isHasMore = true
    var movieDiscoverLiveData: SingleLiveEvent<List<MovieDiscover?>>
    var movieDiscoverUpdateLiveData: SingleLiveEvent<List<MovieDiscover?>>
    var genresLiveData: SingleLiveEvent<List<MovieGenre>>
    var errorResp: SingleLiveEvent<Any?>

    var showLoading: SingleLiveEvent<Boolean?>
    var showErrorLayout: SingleLiveEvent<Boolean?>
    private var repo: MovieDiscoverRepo? = null

    private var isMovieDiscoverSuccess = false
    private var isMovieGenreSuccess = false

    init {
        movieDiscoverLiveData = SingleLiveEvent()
        movieDiscoverUpdateLiveData = SingleLiveEvent()
        genresLiveData = SingleLiveEvent()
        errorResp = SingleLiveEvent()
        showLoading = SingleLiveEvent()
        showErrorLayout = SingleLiveEvent()

        genres = arrayListOf()
        movies = arrayListOf()
    }

    fun setupRepo(repository: MovieDiscoverRepo) {
        this.repo = repository
    }

    fun getIsHasMoreData():Boolean{
        return isHasMore
    }

    fun getGenreList():List<MovieGenre>{
        return genres
    }

    private fun updateIsMovieDiscoverSuccess(isSuccess : Boolean){
        isMovieDiscoverSuccess = isSuccess
     }

     fun getIsMovieDiscoverSuccess():Boolean = isMovieDiscoverSuccess

     fun getIsMovieGenreSuccess():Boolean =  isMovieGenreSuccess

    private fun updateIsMovieGenreSuccess(isSuccess: Boolean){
        isMovieGenreSuccess = isSuccess
    }

    fun initCheckMovieLocalData(){
        if(movies.size >0){
            movieDiscoverLiveData.value = movies
        }else{
            fetchMovieDiscover()
        }
    }

    private fun resetPageAndMovie() {
        page = 1
        isHasMore = true
        movies.clear()
    }

    fun handleProgressBar() {
        if (page == 1) {
            showLoading.value = true
        }
    }

    private fun dismissLoading() {
        if (showLoading.value == true)
            showLoading.value = false
    }

    fun fetchMovieDiscover() {
        if(isHasMore) {
            handleProgressBar()
            viewModelScope.launch {
                val resp = repo?.getMovieList(genre, page)
                dismissLoading()
                if (resp?.status == Status.SUCCESS) {
                    handleSuccessFetchMovieList(resp)
                } else {
                    updateIsMovieDiscoverSuccess(false)
                    handleErrorResponse(resp)
                }
            }
        }else{
            errorResp.value = R.string.no_more_data
        }
    }

    fun fetchMovieGenres() {
        viewModelScope.launch {
            val resp = repo?.getGenreList()
            dismissLoading()
            if (resp?.status == Status.SUCCESS) {
                handleSuccessFetchGenreList(resp)
            } else {
                updateIsMovieGenreSuccess(false)
                handleErrorResponse(resp)
            }
        }
    }

    fun fetchMovieWithGenre(newGenre: String?){
        if(newGenre.isNullOrEmpty()) genre = "" else genre = newGenre
        resetPageAndMovie()
        fetchMovieDiscover()
    }

    private fun handleShowErrorLayout() {
        showErrorLayout.value = page == 1
    }

    private fun handleSuccessFetchMovieList(resp: LiveDataResult<Any?>) {
        if (resp.data != null) {
            try {
                val data = resp.data as MovieDiscoverResp
                isHasMore = data.results.isNotEmpty()
                handleMoviesData(data.results)
                updateIsMovieDiscoverSuccess(true)
                page++

            } catch (e: Exception) {
                updateIsMovieDiscoverSuccess(false)
                errorResp.value = e.message
            }
        } else {
            updateIsMovieDiscoverSuccess(false)
            errorResp.value = R.string.error_api_message
        }
    }

    private fun handleSuccessFetchGenreList(resp: LiveDataResult<Any?>) {
        if (resp.data != null) {
            try {
                val data = resp.data as MovieGenreResp
                genres.clear()
                genres.addAll(data.genres)
                genresLiveData.value = genres
                updateIsMovieGenreSuccess(true)

            } catch (e: Exception) {
                updateIsMovieGenreSuccess(false)
                errorResp.value = e.message
            }
        } else {
            updateIsMovieGenreSuccess(false)
            errorResp.value = R.string.error_api_message
        }
    }

    private fun handleMoviesData(data : List<MovieDiscover>?){
        if(data!=null){
            if(page == 1)
                movieDiscoverLiveData.value = data
            else
                movieDiscoverUpdateLiveData.value = data

            this.movies.addAll(data)
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