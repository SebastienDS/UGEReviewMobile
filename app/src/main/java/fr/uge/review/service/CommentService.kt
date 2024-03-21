package fr.uge.review.service

import androidx.compose.foundation.pager.PageSize
import fr.uge.review.dto.like.LikeStateDTO
import fr.uge.review.dto.review.CreateReviewDTO
import fr.uge.review.dto.review.ReviewCreatedDTO
import fr.uge.review.dto.review.ReviewOneReviewDTO
import fr.uge.review.dto.review.ReviewsDTO
import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserLoginDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {
    @POST("/api/v1/comments/{commentId}/like")
    fun likeComment(@Path("commentId") commentId: Long): Call<LikeStateDTO>

    @POST("/api/v1/comments/{commentId}/dislike")
    fun dislikeComment(@Path("commentId") commentId: Long): Call<LikeStateDTO>
}
