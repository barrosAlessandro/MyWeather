package br.com.wcabral.myweather.extension

import android.content.res.Resources

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()