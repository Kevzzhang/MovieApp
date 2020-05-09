package com.kevin.movieapp.network

import com.kevin.movieapp.ui.MyApplication.Companion.getContext
import com.kevin.movieapp.utils.AppConstant
import com.kevin.movieapp.utils.cache.SSLInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {
    val client: Retrofit
        get() {
            val retrofit: Retrofit

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            var client: OkHttpClient? = null


            client = OkHttpClient.Builder()
                .connectTimeout(AppConstant.TIME_OUT_LIMIT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(AppConstant.TIME_OUT_LIMIT.toLong(), TimeUnit.SECONDS)
                .readTimeout(AppConstant.TIME_OUT_LIMIT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(ChuckInterceptor(getContext()))
                .addInterceptor(SSLInterceptor(getContext()!!))
                .addInterceptor(ConnectivityInterceptor(getContext()!!))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(AppConstant.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit
        }
}