package fr.uge.review.broadcastReceiver

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.uge.review.ApiClient
import fr.uge.review.handleCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewAnswerFetchService : Service(), CoroutineScope {

    companion object {
        const val TIME = 1 * 1000L
    }

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var apiClient: ApiClient
    private var numberNotifications: Int = 0

    override fun onCreate() {
        super.onCreate()
        apiClient = ApiClient(this)
        handleCall(apiClient.notificationService.fetchNotifications()) { notificationsList ->
            numberNotifications = notificationsList.size
        }
        startPeriodicTask(TIME)
        Log.i("ServiceStarted", "IM SO HAPPY")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun startPeriodicTask(interval: Long) {
        launch {
            while (isActive) {
                Log.i("periodicTask", "Prayge")
                fetchNewNotifications()
                delay(interval)
            }
        }
    }

    private fun fetchNewNotifications() {
        handleCall(apiClient.notificationService.fetchNotifications()) { notificationsList ->
            if(numberNotifications < notificationsList.size){
                Log.i("SendNotif", "NOTIF")
                sendNotification()
            }
            numberNotifications = notificationsList.size
        }
    }

    private fun sendNotification() {
        val broadcastIntent = Intent("fr.uge.review.NOTIFICATION_RECEIVED")
        val componentName = ComponentName(this, NewAnswerReceiver::class.java)
        broadcastIntent.component = componentName
        sendBroadcast(broadcastIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
