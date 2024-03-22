package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.review.dto.notification.NotificationDTO
import fr.uge.review.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun fetchNotifications(apiClient: ApiClient, onSuccess: (List<NotificationDTO>) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.notificationService.fetchNotifications()
        .enqueue(object : Callback<List<NotificationDTO>> {
            override fun onFailure(call: Call<List<NotificationDTO>>, t: Throwable) {
                Log.e("UwU",  "OwO notifications", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<List<NotificationDTO>>, response: Response<List<NotificationDTO>>) {
                if (response.isSuccessful) {
                    val notifications = response.body()!!
                    Log.i("UwU", notifications.toString())
                    onSuccess(notifications)
                } else {
                    Log.e("UwU", "OwO notifications FAIL")
                    onFailure(null)
                }
            }
        })
}

@Composable
fun Notifications(navController: NavHostController, apiClient: ApiClient, sessionManager: SessionManager) {
    var notifications by remember { mutableStateOf<List<NotificationDTO>>(listOf()) }

    LaunchedEffect(Unit) {
        fetchNotifications(apiClient, onSuccess = {
            notifications = it
        }, {})
    }
    Column {
        NotificationsViewer(navController, apiClient, notifications, modifier = Modifier
            .weight(1f)
            .background(Color.White)
            .fillMaxWidth()) { notification ->
            notifications = notifications.filter { it != notification }
        }
        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun NotificationsViewer(navController: NavHostController, apiClient: ApiClient, notifications: List<NotificationDTO>, modifier: Modifier, onMarkAsRead: (NotificationDTO) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(notifications) {
            Notification(navController, apiClient, it, Modifier.fillMaxWidth(), onMarkAsRead)
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

@Composable
fun Notification(navController: NavHostController, apiClient: ApiClient, notification: NotificationDTO, modifier: Modifier, onMarkAsRead: (NotificationDTO) -> Unit) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(100.dp), contentAlignment = Alignment.Center) {
            Icon(
                Icons.Default.Clear, Modifier
                    .fillMaxHeight()
                    .padding(4.dp)) {
                markAsRead(apiClient = apiClient, notification = notification,
                    onSuccess = {
                        onMarkAsRead(notification)
                    }, onFailure = {})
            }
        }

        Box(
            Modifier
                .weight(1f)
                .padding(8.dp)
                .clickable {
                    markAsRead(apiClient = apiClient, notification = notification,
                        onSuccess = {
                            val regex = "/reviews/(?<id>\\d+)".toRegex()
                            val match = regex.find(notification.link)!!
                            val id = match.groups["id"]!!.value
                            navController.navigate("Review/$id")
                        }, onFailure = {})
                }) {
            Text(notification.message, color = Color.Black)
        }
    }
}

fun markAsRead(apiClient: ApiClient, notification: NotificationDTO, onSuccess: () -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.notificationService.markAsRead(notification.id)
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO markAsRead", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU markAsRead $notification")
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO markAsRead FAIL")
                    onFailure(null)
                }
            }
        })
}