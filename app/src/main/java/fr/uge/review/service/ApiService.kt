package fr.uge.review.service

import fr.uge.review.dto.ReviewDTO
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/api/v1/reviews")
    fun fetchData(): Call<List<ReviewDTO>>
}