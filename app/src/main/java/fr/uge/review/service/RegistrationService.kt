package fr.uge.review.service

import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserLoginDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationService {

    @POST("/api/v1/login")
    fun login(@Body request: UserLoginDTO): Call<UserDataDTO>

}