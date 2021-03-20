package br.com.wcabral.myweather.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isInternetAvailable(): Boolean {
    var result = false
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
        result = when {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    return result
}