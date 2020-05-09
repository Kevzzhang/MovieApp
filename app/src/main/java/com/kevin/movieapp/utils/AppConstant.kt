package com.kevin.movieapp.utils

class AppConstant{
    companion object{
        const val API_BASE_URL="https://api.themoviedb.org/3/"
        const val API_KEY ="8278afe97201cb3a38eb878dfc900631"
        const val API_LANGUAGE = "en-US"
        const val API_KEY_AND_LANGUAGE = "?api_key=${API_KEY}&language=${API_LANGUAGE}"

        const val API_DISCOVER ="discover/movie$API_KEY_AND_LANGUAGE"
        const val API_GENRE_MOVIE = "genre/movie/list$API_KEY_AND_LANGUAGE"
        const val API_DETAIL ="movie"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

        const val WIFI_ENABLED_STATE= "Wifi enabled"
        const val MOBILE_NETWORK_STATE= "Mobile data enabled"
        const val NO_INTERNET_STATE= "Not connected to Internet"

        const val TIME_OUT_LIMIT = 20
    }
}