package fr.uge.review.service

import fr.uge.review.dto.user.UserSignUpDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/api/v1/signup")
    fun signup(@Body request: UserSignUpDTO): Call<Void>

}