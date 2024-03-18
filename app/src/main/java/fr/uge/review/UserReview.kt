package fr.uge.review

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.review.dto.review.ReviewsDTO
import fr.uge.review.service.SessionManager
import retrofit2.Call
import retrofit2.Callback

fun fetchUserReviews(userId: Long, page: Int, apiClient: ApiClient, onSuccess: (List<ReviewsDTO>) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.userService.fetchUserReviews(userId, page, 20)
        .enqueue(object : Callback<List<ReviewsDTO>> {
            override fun onFailure(call: Call<List<ReviewsDTO>>, t: Throwable) {
                Log.e("UwU",  "OwO review", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<List<ReviewsDTO>>, response: retrofit2.Response<List<ReviewsDTO>>) {
                if (response.isSuccessful) {
                    val reviews = response.body()!!
                    Log.i("UwU", reviews.toString())
                    onSuccess(reviews)
                } else {
                    Log.e("UwU", "OwO Review FAIL")
                    onFailure(null)
                }
            }
        })
}

@Composable
fun UserReviews(
    navController: NavHostController,
    userId: Long,
    sessionManager: SessionManager,
    apiClient: ApiClient
){
    var reviews: List<ReviewsDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }
    fetchUserReviews(userId, page, apiClient, {reviews = it}, {})

    Column {
        Content(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
            reviews = reviews,
            previous = {
                page--
            },
            next = {
                page++
            })
        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}