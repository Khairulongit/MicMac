package com.tchnodynamic.micmac

import android.annotation.TargetApi
import android.content.*
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData


@Suppress("DEPRECATION")
class NetworkConnection(private val context: Context):LiveData<Boolean>() {
    private  var connectivityManager:ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var newWorkCallbacks: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        when{
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.N -> {

                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
            }
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP->{
                lolipopNwtWorkRequest()
            }
            else->{
                context.registerReceiver(
                        networkReceiver,
                        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }

        }
    }

//    override fun onInactive() {
//        super.onInactive()
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())
//        }else{
//
//            try {
//                context.unregisterReceiver(networkReceiver)
//            } catch (e: IllegalArgumentException) {
//                System.out.printf(e.message)
//            }
////            context.unregisterReceiver(networkReceiver)
//        }
//    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lolipopNwtWorkRequest(){
        val requestBuilder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        connectivityManager.registerNetworkCallback(
                requestBuilder.build(), connectivityManagerCallback()
        )
    }

    private fun connectivityManagerCallback():ConnectivityManager.NetworkCallback{
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            newWorkCallbacks = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(false)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(true)

                }

            }
            return newWorkCallbacks
        }else{
            throw IllegalAccessError("Error")
        }
    }


    private val networkReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            updateConnection()
        }

    }


    private fun updateConnection(){
        val activenetwork:NetworkInfo?=connectivityManager.activeNetworkInfo
        postValue(activenetwork?.isConnected == true)
    }
}