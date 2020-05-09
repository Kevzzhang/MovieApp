package com.kevin.movieapp.model.movie_video

import com.google.gson.annotations.SerializedName

data class MovieVideo (
    val id : String,
    @SerializedName("key")
    val url : String,
    val name : String,
    val site : String,
    val size : Int,
    val type : String
){
}