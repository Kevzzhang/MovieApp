package com.kevin.base_application.utils

import android.content.Context
import android.net.ConnectivityManager
import com.kevin.movieapp.utils.AppConstant

object NetworkUtil {
    var TYPE_WIFI = 1
    var TYPE_MOBILE = 2
    var TYPE_NOT_CONNECTED = 0

    fun getConnectivityStatus(context: Context): Int {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        if (conn == TYPE_WIFI) {
            status = AppConstant.WIFI_ENABLED_STATE
        } else if (conn == TYPE_MOBILE) {
            status = AppConstant.MOBILE_NETWORK_STATE
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = AppConstant.NO_INTERNET_STATE
        }
        return status
    }
}