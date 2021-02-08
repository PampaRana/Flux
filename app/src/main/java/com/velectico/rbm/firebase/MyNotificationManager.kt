package com.velectico.rbm.firebase

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Html
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MyNotificationManager(mCtx: Context) {
    private val mCtx: Context

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    fun showBigNotification(
        title: String?,
        message: String?,
        url: String,
        intent: Intent?
    ) {
        val resultPendingIntent = PendingIntent.getActivity(
            mCtx,
            ID_BIG_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val bigPictureStyle: NotificationCompat.BigPictureStyle =
            NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(title)
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        bigPictureStyle.bigPicture(getBitmapFromURL(url))
        val mBuilder= NotificationCompat.Builder(mCtx)
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.mipmap.sym_def_app_icon).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(title)
            .setStyle(bigPictureStyle)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.sym_def_app_icon))
            .setContentText(message)
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        val notificationManager =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            ID_BIG_NOTIFICATION,
            notification
        )
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    fun showSmallNotification(
        title: String?,
        message: String?,
        intent: Intent?
    ) {
        val resultPendingIntent = PendingIntent.getActivity(
            mCtx,
            ID_SMALL_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder= NotificationCompat.Builder (mCtx)
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.mipmap.sym_def_app_icon).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(title)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.sym_def_app_icon))
            .setContentText(message)
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        val notificationManager =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            ID_SMALL_NOTIFICATION,
            notification
        )
    }

    //The method will return Bitmap from an image URL
    private fun getBitmapFromURL(strURL: String): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        const val ID_BIG_NOTIFICATION = 234
        const val ID_SMALL_NOTIFICATION = 235
    }

    init {
        this.mCtx = mCtx
    }
}