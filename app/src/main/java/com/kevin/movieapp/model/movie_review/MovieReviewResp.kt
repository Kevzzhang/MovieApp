package com.kevin.movieapp.model.movie_review

import com.kevin.movieapp.model.movie_review.MovieReview

data class MovieReviewResp(
    val id : String,
    val page : String,
    val results : List<MovieReview>
){

}