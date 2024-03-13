package fr.uge.review.service

import fr.uge.review.dto.review.ReviewOneReviewDTO
import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserLoginDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {

    @GET("/api/v1/reviews/{reviewId}")
    fun fetchReview(@Path("reviewId") reviewId: Long): Call<ReviewOneReviewDTO>

}