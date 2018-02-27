package cinemax.indo.aliagus.com.indocinemax.lib

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by ali on 25/02/18.
 */
object ConnectionProvider {
    fun networkStatus(context: Context): Boolean? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }
}