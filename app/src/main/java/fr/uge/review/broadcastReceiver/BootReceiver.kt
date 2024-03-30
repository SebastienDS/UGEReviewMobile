package fr.uge.review.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, NewAnswerFetchService::class.java)
        context.startService(serviceIntent)
        Log.i("BootReceived", "IT WORKS !!!!")
    }
}