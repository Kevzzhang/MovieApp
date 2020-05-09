package com.kevin.movieapp.utils.cache

import android.content.Context
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

class SSLInterceptor(private val mContext: Context) : Interceptor {

    private fun initializeSSLContext(mContext: Context) {
        try {
            SSLContext.getInstance("TLSv1.2")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        try {
            ProviderInstaller.installIfNeeded(mContext.applicationContext)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        initializeSSLContext(mContext)
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
