package fr.uge.review.service

import fr.uge.review.dto.resetPassword.AskEmailDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ResetPasswordService {

    @POST("/api/v1/resetPassword")
    fun resetPassword(@Body email: AskEmailDTO): Call<Void>

}