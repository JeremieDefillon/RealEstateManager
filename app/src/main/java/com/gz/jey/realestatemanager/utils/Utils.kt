package com.gz.jey.realestatemanager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.gz.jey.realestatemanager.models.Data
import java.text.SimpleDateFormat
import java.util.*

object Utils{

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int = Math.round(dollars * 0.812).toInt()

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param euros
     * @return
     */
    fun convertEuroToDollar(euros: Int): Int = Math.round(euros / 0.812).toInt()

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        return dateFormat.format(Date())
    }

    /**
     * convert today date in string format
     * @return String as dd/mm/yyyy format
     */
    fun getTodayDateStr(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(Date())
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return Boolean
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }

    /**
     * check if device is connected to internet
     * @param context
     * @return Boolean
     */
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = cm.activeNetworkInfo
        return activeNetwork.isConnectedOrConnecting
    }

    /**
     * convert today date in string format
     * @return String as dd/mm/yyyy format
     */
    fun convertedHighPrice(text : String): String {
        var hp : String
        val s = if(Data.currency==0) "," else "."
        val end = text.length
        val step = 3
        val over = end-1
        val mod = text.length%step
        val start = mod+1
        if(end>3){
            hp = text.substring(0,mod)
            for (i in start until end+1 step step){
                val init = i-1
                val max = i+2
                hp += when {
                    i==end && mod!=0 -> s + text.substring(init,over)
                    mod==0 && i==start && i!=end -> text.substring(0,step)
                    mod==0 && i!=start && i==end -> text.substring(init,over)
                    mod==0 && i!=start && i!=end -> s + text.substring(init, max)
                    else -> s + text.substring(init, max)
                }
            }
        }else
            hp = text

        return hp
    }
}