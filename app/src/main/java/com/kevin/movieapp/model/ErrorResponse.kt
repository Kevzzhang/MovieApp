package com.kevin.movieapp.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status_code")
    val code : Int?,
    @SerializedName("status_message")
    val message : String?
){

}