package com.kevin.movieapp.model.movie_genre

import android.os.Parcelable
import com.kevin.movieapp.model.movie_genre.MovieGenre
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieGenreResp(
    val genres : List<MovieGenre>
):Parcelable{}