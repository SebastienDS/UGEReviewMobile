package fr.uge.review.service

import fr.uge.review.dto.review.CreateReviewDTO
import fr.uge.review.dto.review.ReviewCreatedDTO
import fr.uge.review.dto.review.ReviewOneReviewDTO
import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserLoginDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {

    @GET("/api/v1/reviews/{reviewId}")
    fun fetchReview(@Path("reviewId") reviewId: Long): Call<ReviewOneReviewDTO>

    @POST("/api/v1/createReview")
    fun createReview(@Body body: RequestBody): Call<ReviewCreatedDTO>
}

fun ReviewService.createReview(review: CreateReviewDTO): Call<ReviewCreatedDTO> {
    val body = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("title", review.title)
        .addFormDataPart("commentary", review.commentary)
        .addFormDataPart("code", review.code)
        .addFormDataPart("test", review.test)
        .build()
    return createReview(body)
}