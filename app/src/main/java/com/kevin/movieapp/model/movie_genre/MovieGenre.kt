package com.kevin.movieapp.model.movie_genre

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieGenre(
    val id : Int? = null,
    val name : String? = ""
):Parcelable{}
