package com.gz.jey.realestatemanager.controllers.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.Utils
import java.text.SimpleDateFormat
import java.util.*


class NotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "4565"
        private const val NOTIFICATION_CHANNEL_NAME = "REM"
        private const val REAL_ESTATE = "REAL_ESTATE"
        private const val NOTIF = "FROM_NOTIF"
    }

    // Activity
    private lateinit var re : RealEstate
    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE)

    /**
     * @param context Context
     * @param intent Intent
     * ON RECEIVING NOTIFICATIONS FROM ALARM MANAGER
     */
    override fun onReceive(context: Context, intent: Intent) {
        val extra = intent.extras!!
        Log.d("NOTIF", "STARTING")
        re = extra.getParcelable(REAL_ESTATE)
        buildNotification(context)
    }


    /**
     * @param context Context
     * BUILD NOTIFICATION
     */
    private fun buildNotification(context: Context) {

        val coming : ArrayList<String> = ArrayList()
        coming.clear()

        val notificationManager: NotificationManager?
        //Notification Channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)

            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            assert(true)
            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        val repeatingIntent = Intent(context, MainActivity::class.java)
        repeatingIntent.putExtra(NOTIF, true)
        repeatingIntent.putExtra(Code.RE_ID, re.id)
        repeatingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(context, 987,
                repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val l1 = getFirstLine(context, re.type, re.room, re.surface)
        val l2 = getSecondLine(context, re.locality, re.price, re.surface)
        val l3 = Utils.convertedHighPrice(context, re.price)

        val addoedit = if (Data.isEdit) context.getString(R.string.edited_re) else context.getString(R.string.added_new_re)

        val sb : StringBuilder = StringBuilder()
                .append(l1).append("\r\n")
                .append(l2).append("\r\n")
                .append(l3)

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.main_logo)
                .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                .setContentTitle("Real Estate Manager - $addoedit")
                .setContentText(l1)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(sb.toString()))
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        assert(true)
        notificationManager.notify(987, builder.build())
    }

    private fun getFirstLine(context: Context, type : Int?, room : Int?, surface: Int?) : String{
        val res = context.resources
        val sb = StringBuilder()
        if(type!=null) sb.append(res.getStringArray(R.array.type_ind)[type]).append("  ")
        if(room!=null) sb.append(Utils.getRoomNumFormat(context, room)).append("  ")
        if(room!=null) sb.append(Utils.getSurfaceFormat(context, surface)).append("  ")
        return if(sb.isNotEmpty()) sb.substring(0, sb.toString().length-1)
        else ""
    }

    private fun getSecondLine(context: Context, locality : String, price : Long?, surface: Int?): String{
        val sb = StringBuilder()
        if(locality.isNotEmpty()) sb.append("($locality)").append("  ")
        sb.append(Utils.getPPMFormat(context, price, surface)).append("  ")
        return if(sb.isNotEmpty()) sb.substring(0, sb.toString().length-1)
        else ""
    }

}
