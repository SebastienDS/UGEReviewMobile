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

interface ResponseService {
    @POST("/api/v1/responses/{responseId}/like")
    fun likeResponses(@Path("responseId") commentId: Long): Call<LikeStateDTO>

    @POST("/api/v1/responses/{responseId}/dislike")
    fun dislikeResponses(@Path("responseId") commentId: Long): Call<LikeStateDTO>
}
