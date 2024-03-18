package fr.uge.review

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

fun fetchUserComments(userId: Long, page: Int, apiClient: ApiClient, onSuccess: (List<CommentUserDTO>) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.userService.fetchUserComments(userId, page, 20)
        .enqueue(object : Callback<List<CommentUserDTO>> {
            override fun onFailure(call: Call<List<CommentUserDTO>>, t: Throwable) {
                Log.e("UwU",  "OwO review", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<List<CommentUserDTO>>, response: retrofit2.Response<List<CommentUserDTO>>) {
                if (response.isSuccessful) {
                    val comments = response.body()!!
                    Log.i("UwU", comments.toString())
                    onSuccess(comments)
                } else {
                    Log.e("UwU", "OwO Review FAIL")
                    onFailure(null)
                }
            }
        })
}

@Composable
fun UserComments(
    navController: NavHostController,
    userId: Long,
    sessionManager: SessionManager,
    apiClient: ApiClient
){
    var comments: List<CommentUserDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(page, userId) {
        fetchUserComments(userId, page, apiClient, { comments = it }, {})
    }

    Column {
        Content(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
            showAbles = comments,
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