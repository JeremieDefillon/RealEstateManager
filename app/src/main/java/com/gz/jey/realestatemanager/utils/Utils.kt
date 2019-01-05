package com.gz.jey.realestatemanager.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.models.Data

object Utils{

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Long): Long = Math.round(dollars * 0.812)

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param euros
     * @return
     */
    fun convertEuroToDollar(euros: Long): Long = Math.round(euros / 0.812)

    /**
     * Convert a given date into fr date format
     * @return String as dd/mm/yyyy format
     */
    fun getDateFr(date : String): String {
        val dts = date.split("-")
        return if(dts.size==3) dts[2]+"/"+dts[1]+"/"+dts[0] else ""
    }

    /**
     * Convert a given date into en date format
     * @return String as yyyy/mm/dd format
     */
    fun getDateEn(date : String): String {
        val dts = date.split("-")
        return if(dts.size==3) dts[0]+"/"+dts[1]+"/"+dts[2] else ""
    }

    /**
     * convert today date in string format
     * @return String as dd/mm/yyyy format
     */
    fun convertedHighPrice(context : Context, price : Long?): String {
        if(price!=null){
            val text = price.toString()
            var hp : String
            val s = if(Data.currency==0) "," else " "
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

            return if(Data.currency==1) hp + " " + context.getString(R.string.euro_symbol)
            else context.getString(R.string.dollar_symbol) + " " + hp
        }else
            return context.getString(R.string.price) + " " + context.getString(R.string.nc)
    }

    /**
     * TO GET SURFACE IN PROPER FORMAT
     * @param context Context
     * @param surface Int
     * @return String
     */
    fun getSurfaceFormat(context: Context, surface : Int?) : String{
        return if(surface!=null) surface.toString() + " " + context.getString(R.string.m2)
        else context.getString(R.string.surface) + " " + context.getString(R.string.nc)
    }

    /**
     * TO GET ROOM NUMBERS IN PROPER FORMAT
     * @param context Context
     * @param room Int
     * @return String
     */
    fun getRoomNumFormat(context : Context, room : Int?) : String{
        val r = if(room!=null && room>1) context.getString(R.string.rooms)
            else context.getString(R.string.room)

        return if(room!=null) "$room $r"
        else "? $r"
    }


    /**
     * TO GET PRICE PER M² IN PROPER FORMAT
     * @param context Context
     * @param price Long
     * @param surface Int
     * @return String
     */
    fun getPPMFormat(context : Context, price : Long?, surface : Int?) : String{

        return if(price!=null && surface!=null){
            val part : Long = (price/surface)
            val str = this.convertedHighPrice(context, part)
           // val partStr = String.format("%.2f", str)
             str + "/" + context.getString(R.string.m2)
        }else{
            context.getString(R.string.price)+"/"+context.getString(R.string.m2)+" "+context.getString(R.string.nc)
        }
    }

    /**
     * TO GET LOCATION IN PROPER FORMAT
     * @param a String
     * @param b String
     * @param c String
     * @param d String
     * @param e String
     * @return String
     */
    fun formatLocation(a:String?, b:String?, c:String?, d:String?, e:String?) : String{
        val sb = StringBuilder()
        if(!a.isNullOrEmpty()) sb.append("$a,\r\n")
        if(!b.isNullOrEmpty()) sb.append("$b,\r\n")
        if(!c.isNullOrEmpty()) sb.append("$c,\r\n")
        if(!c.isNullOrEmpty()) sb.append("$d,\r\n")
        if(!d.isNullOrEmpty()) sb.append("$e,\r\n")

        return if(sb.toString().length > 3)
            sb.substring(0, sb.length-3)
        else ""
    }

    /**
     * TO GET BACK TO MAIN ACTIVITY
     * @param activity Activity
     */
    fun backToMainActivity(activity : Activity){
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    /**
     * TO KNOW IF IN LANDSCAPE OR PORTRAIT
     * @param context Context
     * @return Boolean
     */
    fun isLandscape(context: Context): Boolean{
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = cm.activeNetworkInfo
        return activeNetwork.isConnectedOrConnecting
    }
}