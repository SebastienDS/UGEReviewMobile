package fr.uge.review.service

import fr.uge.review.dto.comment.CommentDTO
import fr.uge.review.dto.like.LikeStateDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {
    @POST("/api/v1/comments/{commentId}/like")
    fun likeComment(@Path("commentId") commentId: Long): Call<LikeStateDTO>

    @POST("/api/v1/comments/{commentId}/dislike")
    fun dislikeComment(@Path("commentId") commentId: Long): Call<LikeStateDTO>
    @POST("/api/v1/reviews/{reviewId}/comment")
    fun createComment(@Path("reviewId") reviewId: Long, @Body content: String): Call<CommentDTO>

    @POST("/api/v1/deleteComment")
    fun deleteComment(@Query("reviewId") reviewId: Long, @Body commentId: Long): Call<Void>
}
