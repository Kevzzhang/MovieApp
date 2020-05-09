package com.kevin.movieapp.model.movie_detail

import com.google.gson.annotations.SerializedName
import com.kevin.movieapp.model.movie_genre.MovieGenre

data class MovieDetail(
    val id : Int?,
    @SerializedName("backdrop_path")
    val backdrop : String?,
    val genres : List<MovieGenre>?,
    @SerializedName("original_title")
    val title : String?,
    val overview : String?,
    @SerializedName("poster_path")
    val poster : String?,
    @SerializedName("production_companies")
    val companies : List<ProductionCompany>?,
    @SerializedName("release_date")
    val releaseDate : String?,
    val status : String?,
    val tagline : String?,
    @SerializedName("vote_average")
    val rating : String?
){}
