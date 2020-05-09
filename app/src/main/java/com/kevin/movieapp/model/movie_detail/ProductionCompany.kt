package com.kevin.movieapp.model.movie_detail

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    val id: Int?,
    @SerializedName("logo_path")
    val logo: String?,
    val name : String?,
    @SerializedName("origin_country")
    val country : String?
) {}
