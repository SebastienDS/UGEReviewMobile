package fr.uge.review

import fr.uge.review.dto.like.LikeDTO

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


@Composable
fun UserLikes(
    navController: NavHostController,
    userId: Long,
    sessionManager: SessionManager,
    apiClient: ApiClient
){
    var likes: List<LikeDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }

    LaunchedEffect(page, userId) {
        handleCall(apiClient.userService.fetchUserLikes(userId, page, 20)) {
            likes = it
        }
    }

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