package fr.uge.review

import fr.uge.review.dto.like.LikeDTO

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
import fr.uge.review.dto.comment.CommentUserDTO
import fr.uge.review.service.SessionManager
import retrofit2.Call
import retrofit2.Callback

fun fetchUserLikes(userId: Long, page: Int, apiClient: ApiClient, onSuccess: (List<LikeDTO>) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.userService.fetchUserLikes(userId, page, 20)
        .enqueue(object : Callback<List<LikeDTO>> {
            override fun onFailure(call: Call<List<LikeDTO>>, t: Throwable) {
                Log.e("UwU",  "OwO review", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<List<LikeDTO>>, response: retrofit2.Response<List<LikeDTO>>) {
                if (response.isSuccessful) {
                    val likes = response.body()!!
                    Log.i("UwU", likes.toString())
                    onSuccess(likes)
                } else {
                    Log.e("UwU", "OwO Review FAIL")
                    onFailure(null)
                }
            }
        })
}

@Composable
fun UserLikes(
    navController: NavHostController,
    userId: Long,
    sessionManager: SessionManager,
    apiClient: ApiClient
){
    var likes: List<LikeDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }
    fetchUserLikes(userId, page, apiClient, {likes = it}, {})

    Column {
        Content(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
            showAbles = likes,
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