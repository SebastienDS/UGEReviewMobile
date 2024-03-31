package fr.uge.review

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
import fr.uge.review.dto.review.ReviewsDTO
import fr.uge.review.service.SessionManager

@Composable
fun UserReviews(
    navController: NavHostController,
    userId: Long,
    sessionManager: SessionManager,
    apiClient: ApiClient
){
    var reviews: List<ReviewsDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }

    LaunchedEffect(page, userId) {
        handleCall(apiClient.userService.fetchUserReviews(userId, page, 20)) {
            reviews = it
        }
    }

    Column {
        Content(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
            "Users/$userId/reviews",
            showAbles = reviews,
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