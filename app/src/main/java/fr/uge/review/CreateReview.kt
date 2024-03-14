package fr.uge.review

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import fr.uge.review.dto.review.CreateReviewDTO
import fr.uge.review.dto.review.ReviewCreatedDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.service.createReview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CreateReview(navController: NavHostController, apiClient: ApiClient, sessionManager: SessionManager) {
    LaunchedEffect(Unit) {
        createReview(
            apiClient = apiClient,
            review = CreateReviewDTO("title", "commentary", "code", "test"),
            onSuccess = { navController.navigate("Review/${it.id}") },
            onFailure = {}
        )
    }
}

fun createReview(apiClient: ApiClient, review: CreateReviewDTO, onSuccess: (ReviewCreatedDTO) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.reviewService.createReview(review)
        .enqueue(object : Callback<ReviewCreatedDTO> {
            override fun onFailure(call: Call<ReviewCreatedDTO>, t: Throwable) {
                Log.e("UwU",  "OwO test createReview", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<ReviewCreatedDTO>, response: Response<ReviewCreatedDTO>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU test createReview SUCCESS")
                    val body = response.body()!!
                    Log.i("UwU", body.toString())
                    onSuccess(body)
                } else {
                    Log.e("UwU", "OwO test createReview FAIL")
                    onFailure(null)
                }
            }
        })
}