package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.comment.CommentDTO
import fr.uge.review.dto.review.ReviewOneReviewDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat

@Composable
fun Review(
    navController: NavHostController,
    reviewId: Long,
    apiClient: ApiClient,
    sessionManager: SessionManager,
) {
    var review: ReviewOneReviewDTO? by remember { mutableStateOf(null) }

    LaunchedEffect(reviewId) {
        apiClient.reviewService.fetchReview(reviewId)
            .enqueue(object : Callback<ReviewOneReviewDTO> {
                override fun onFailure(call: Call<ReviewOneReviewDTO>, t: Throwable) {
                    Log.e("UwU",  "OwO review", t)
                }

                override fun onResponse(call: Call<ReviewOneReviewDTO>, response: retrofit2.Response<ReviewOneReviewDTO>) {
                    if (response.isSuccessful) {
                        review = response.body()!!
                        Log.i("UwU", review.toString())
                    } else {
                        Log.e("UwU", "OwO Review FAIL")
                    }
                }
            })
    }

    Column {
        val modifier = Modifier
            .weight(1f)
            .background(Color.White)
            .fillMaxWidth()

        if (review == null) {
            Box(modifier = modifier)
        } else  {
            ReviewViewer(navController, review!!, modifier = modifier)
        }

        Footer(navController, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun ReviewViewer(navController: NavHostController, review: ReviewOneReviewDTO, modifier: Modifier) {
    LazyColumn(modifier) {
        item {
            ReviewHeader(navController, review)
            ReviewContent(review, modifier = Modifier.padding(20.dp, 10.dp))

            Text("31 Réponses:", Modifier.padding(3.dp))

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
        items(review.comments) {
            CommentItem(it)
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

@Composable
fun ReviewHeader(navController: NavHostController, review: ReviewOneReviewDTO) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
           Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.Center) {
               Text(review.title, fontSize = 30.sp)
           }
           Column(Modifier.weight(1f)) {
               Text(review.author.username, Modifier.clickable { navController.navigate("Profile") })
               Text(SimpleDateFormat("dd/MM/yyyy").format(review.date))
           }
        }

        Text(review.commentary, modifier = Modifier.padding(20.dp, 10.dp))
    }
}

@Composable
fun ReviewContent(review: ReviewOneReviewDTO, modifier: Modifier) {
    Column {
        Text(review.code, modifier = modifier
            .border(1.dp, Color.Black)
            .padding(20.dp))
        Text(review.test, modifier = modifier
            .border(1.dp, Color.Black)
            .padding(20.dp))
    }
}

@Composable
fun CommentItem(comment: CommentDTO) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.KeyboardArrowUp, Modifier
                .fillMaxHeight()) {
                Log.i("test", "Poce blo")
            }
            Text("${comment.likes}")
            Icon(Icons.Default.KeyboardArrowDown, Modifier
                .fillMaxHeight()) {
                Log.i("test", "Pas Poce blo")
            }
        }
        Box(Modifier.weight(1f)) {
            Text(text = comment.content, color = Color.Black, modifier = Modifier.padding(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewPreview() {
    ReviewTheme {
    }
}