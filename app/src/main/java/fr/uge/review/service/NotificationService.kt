package fr.uge.review.service

import fr.uge.review.dto.notification.NotificationDTO
import fr.uge.review.dto.notification.NotificationStateDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService {

    @GET("/api/v1/notifications")
    fun fetchNotifications(): Call<List<NotificationDTO>>

    @POST("/api/v1/notifications/{notificationId}/markAsRead")
    fun markAsRead(@Path("notificationId") notificationId: Long): Call<Void>

    @GET("/api/v1/reviews/{reviewId}/notifications/state")
    fun fetchNotificationState(@Path("reviewId") reviewId: Long): Call<NotificationStateDTO>

    @POST("/api/v1/reviews/{reviewId}/notifications/activate")
    fun activateNotification(@Path("reviewId") reviewId: Long): Call<Void>

    @POST("/api/v1/reviews/{reviewId}/notifications/deactivate")
    fun deactivateNotification(@Path("reviewId") reviewId: Long): Call<Void>
}