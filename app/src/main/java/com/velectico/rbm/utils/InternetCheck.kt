package com.velectico.rbm.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


object InternetCheck {
    private var networkInfo: NetworkInfo? = null
    private val countryCode = 0

    /**
     * Is there internet connection
     */
    fun isConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // test for connection for WIFI
        if (networkInfo != null && networkInfo!!.isAvailable
            && networkInfo!!.isConnected
        ) {
            return true
        }
        networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        // test for connection for Mobile
        return (networkInfo != null && networkInfo!!.isAvailable
                && networkInfo!!.isConnected)
    }
}