package com.kevin.movieapp.model.movie_discover

import com.google.gson.annotations.SerializedName

data class MovieDiscoverResp(
    val page : Int?,
    @SerializedName("total_results")
    val total : Int?,
    @SerializedName("total_pages")
    val pages : Int?,
    val results : List<MovieDiscover>
){}