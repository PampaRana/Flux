package com.velectico.rbm.firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.velectico.rbm.navdrawer.views.DashboardActivity
import org.json.JSONException
import org.json.JSONObject


class MyFirebaseMessagingService{ /*: FirebaseMessagingService() {
    @SuppressLint("LogNotTimber")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.size > 0) {
            Log.e(
                TAG,
                "Data Payload: " + remoteMessage.data.toString()
            )
            try {
                val json = JSONObject(remoteMessage.data.toString())
                sendPushNotification(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private fun sendPushNotification(json: JSONObject) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON $json")
        try {
            //getting the json data
            val data = json.getJSONObject("data")

            //parsing json data
            val title = data.getString("title")
            val message = data.getString("message")
            val imageUrl = data.getString("image")

            //creating MyNotificationManager object
            val mNotificationManager =
                MyNotificationManager(applicationContext)

            //creating an intent for the notification
            val intent = Intent(applicationContext, DashboardActivity::class.java)

            //if there is no image
            if (imageUrl == "null") {
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent)
            } else {
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }*/
}