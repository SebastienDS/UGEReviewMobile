package fr.uge.review

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


@Composable
fun Notifications(navController: NavHostController, apiClient: ApiClient, sessionManager: SessionManager) {
    var notifications by remember { mutableStateOf<List<NotificationDTO>>(listOf()) }

    LaunchedEffect(Unit) {
        handleCall(apiClient.notificationService.fetchNotifications()) {
            notifications = it
        }
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
                handleCall(apiClient.notificationService.markAsRead(notification.id)) {
                    onMarkAsRead(notification)
                }
            }
        }

        Box(
            Modifier
                .weight(1f)
                .padding(8.dp)
                .clickable {
                    handleCall(apiClient.notificationService.markAsRead(notification.id)) {
                        val regex = "/reviews/(?<id>\\d+)".toRegex()
                        val match = regex.find(notification.link)!!
                        val id = match.groups["id"]!!.value
                        navController.navigate("Review/$id")
                    }
                }) {
            Text(notification.message, color = Color.Black)
        }
    }
}
