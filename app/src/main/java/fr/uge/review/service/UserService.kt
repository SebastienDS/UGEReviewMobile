package fr.uge.review.service

import fr.uge.review.dto.comment.CommentUserDTO
import fr.uge.review.dto.like.LikeDTO
import fr.uge.review.dto.response.ResponseUserDTO
import fr.uge.review.dto.review.ReviewsDTO
import fr.uge.review.dto.updatePassword.UpdatePasswordDTO
import fr.uge.review.dto.user.UserDTO
import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserFollowStateDTO
import fr.uge.review.dto.user.UserLoginDTO
import fr.uge.review.dto.user.UserProfileDTO
import fr.uge.review.dto.user.UserSignUpDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("/api/v1/users/{userId}/comments")
    fun fetchUserComments(@Path("userId") userId: Long, @Query("pageNumber") page: Int, @Query("pageSize") pageSize: Int): Call<List<CommentUserDTO>>

    @GET("/api/v1/users/{userId}/responses")
    fun fetchUserResponses(@Path("userId") userId: Long, @Query("pageNumber") page: Int, @Query("pageSize") pageSize: Int): Call<List<ResponseUserDTO>>

    @GET("/api/v1/users/{userId}/follows")
    fun fetchUserFriends(@Path("userId") userId: Long, @Query("pageNumber") page: Int, @Query("pageSize") pageSize: Int): Call<List<UserDTO>>

    @GET("/api/v1/users/{userId}/likes")
    fun fetchUserLikes(@Path("userId") userId: Long, @Query("pageNumber") page: Int, @Query("pageSize") pageSize: Int): Call<List<LikeDTO>>

    @GET("/api/v1/users/{userId}")
    fun userProfile(@Path("userId") userId: Long): Call<UserProfileDTO>

    @POST("/api/v1/users/{userId}/follow")
    fun followUser(@Path("userId") userId: Long): Call<Void>

    @POST("/api/v1/users/{userId}/unfollow")
    fun unfollowUser(@Path("userId") userId: Long): Call<Void>

    @GET("/api/v1/users/{userId}/follow/state")
    fun fetchFollowState(@Path("userId") userId: Long): Call<UserFollowStateDTO>

    @PUT("/api/v1/users/{userId}/updateUsername")
    fun updateUsername(@Path("userId") userId: Long, @Body newUsername: String): Call<Void>

    @PUT("/api/v1/users/{userId}/updateEmail")
    fun updateEmail(@Path("userId") userId: Long, @Body newEmail: String): Call<Void>

    @PUT("/api/v1/users/{userId}/updatePassword")
    fun updatePassword(@Path("userId") userId: Long, @Body passwords: UpdatePasswordDTO): Call<Void>
}