package fr.uge.review

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.review.dto.review.ReviewsDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme

@Composable
fun Home(navController: NavHostController, sessionManager: SessionManager, apiClient: ApiClient) {
    var reviews: List<ReviewsDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }

    LaunchedEffect(page) {
        handleCall(apiClient.reviewService.fetchReviews(page, 20)) {
            reviews = it
        }
    }

    Column {
        if (sessionManager.isAuthenticated()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { navController.navigate("CreateReview") }) {
                    Text(stringResource(id = R.string.create))
                }
            }
        }
        Content(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
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

@Composable
fun Content(navController: NavHostController, modifier: Modifier, showAbles: List<ShowAble>?,
            previous: () -> Unit, next: () -> Unit ){
    if(showAbles == null){
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Refresh, Modifier.size(100.dp)) {
                Log.i("UwU", "Refresh")
            }
        }
    }else{
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        Column(modifier = modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier
                .weight(0.8f)
                .fillMaxSize()) {
                items(showAbles) { showAble ->
                    Column(modifier = Modifier
                        .height(80.dp)
                        .fillMaxSize()) {
                        Row(modifier = Modifier.weight(1f).clickable {
                            navController.navigate("Review/${showAble.reviewId()}") }){
                            Text(
                                text = showAble.content(),
                            )
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End

                            ) {
                                Text(text = showAble.author.username, modifier = Modifier.clickable { navController.navigate("Users/${showAble.author.id}") })
                                Text(text = showAble.date.withFormat("${stringResource(id = R.string.date)} hh:mm:ss"))
                            }
                        }
                        Divider(
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Button(
                    onClick = previous,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(text = stringResource(id = R.string.previous))
                }
                Button(
                    onClick = next,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    ReviewTheme {
    }
}