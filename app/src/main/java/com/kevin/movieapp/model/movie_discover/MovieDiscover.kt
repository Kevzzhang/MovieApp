package com.kevin.movieapp.model.movie_discover

import com.google.gson.annotations.SerializedName

data class MovieDiscover(
    val id : Int?,
    @SerializedName("poster_path")
    val cover: String?,
    val title: String?,
    @SerializedName("genre_ids")
    val genre: List<Int>?,
    @SerializedName("release_date")
    val date : String?
) {}