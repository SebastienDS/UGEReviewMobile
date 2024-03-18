package fr.uge.review.service

import fr.uge.review.dto.review.ReviewsDTO
import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserLoginDTO
import fr.uge.review.dto.user.UserSignUpDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @POST("/api/v1/login")
    fun login(@Body request: UserLoginDTO): Call<UserDataDTO>

    @GET("/logout")
    fun logout(): Call<Void>

    @POST("/api/v1/signup")
    fun signup(@Body request: UserSignUpDTO): Call<Void>

    @POST("/api/v1/deleteProfile")
    fun deleteProfile(): Call<Void>

    @GET("/api/v1/users/{userId}/reviews")
    fun fetchUserReviews(@Path("userId") userId: Long, @Query("pageNumber") page: Int, @Query("pageSize") pageSize: Int): Call<List<ReviewsDTO>>
}