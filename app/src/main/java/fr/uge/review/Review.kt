package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
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
import fr.uge.review.dto.response.ResponseDTO
import fr.uge.review.dto.review.ReviewOneReviewDTO
import fr.uge.review.dto.user.Role
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback

fun fetchReview(reviewId: Long, apiClient: ApiClient, onSuccess: (ReviewOneReviewDTO) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.reviewService.fetchReview(reviewId)
        .enqueue(object : Callback<ReviewOneReviewDTO> {
            override fun onFailure(call: Call<ReviewOneReviewDTO>, t: Throwable) {
                Log.e("UwU",  "OwO review", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<ReviewOneReviewDTO>, response: retrofit2.Response<ReviewOneReviewDTO>) {
                if (response.isSuccessful) {
                    val review = response.body()!!
                    Log.i("UwU", review.toString())
                    onSuccess(review)
                } else {
                    Log.e("UwU", "OwO Review FAIL")
                    onFailure(null)
                }
            }
        })
}

fun deleteReview(reviewId: Long, apiClient: ApiClient, onSuccess: () -> Unit) {
    apiClient.reviewService.deleteReview(reviewId)
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO review", t)
            }

            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO Review FAIL")
                }
            }
        })
}

@Composable
fun Review(
    navController: NavHostController,
    reviewId: Long,
    apiClient: ApiClient,
    sessionManager: SessionManager,
) {
    var review: ReviewOneReviewDTO? by remember { mutableStateOf(null) }

    LaunchedEffect(reviewId) {
        fetchReview(reviewId, apiClient, { review = it }, {})
    }

    Column {
        val role = sessionManager.getUserRole()
        if(role == Role.ADMIN) {
            Button(onClick = {
                deleteReview(reviewId, apiClient) {
                    navController.navigate("Home")
                }
            }) {
                Text(text = "Supprimer")
            }
        }
        val modifier = Modifier
            .weight(1f)
            .background(Color.White)
            .fillMaxWidth()

        if (review == null) {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Refresh, Modifier.size(100.dp)) {
                    Log.i("UwU", "Refresh")
                }
            }
        } else  {
            ReviewViewer(navController, review!!, modifier = modifier)
        }

        Footer(navController, sessionManager = sessionManager, modifier = Modifier
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

            val count = computeCommentsCount(review)
            Text("$count Réponses:", Modifier.padding(3.dp))

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }

        items(review.comments) {
            CommentItem(it, navController)
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

fun computeCommentsCount(review: ReviewOneReviewDTO): Int =
    review.comments.sumOf { it.responses.size + 1 }

@Composable
fun ReviewHeader(navController: NavHostController, review: ReviewOneReviewDTO) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
           Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.Center) {
               Text(review.title, fontSize = 30.sp)
           }
           Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
               Text(review.author.username, Modifier.clickable { navController.navigate("User/${review.author.id}") })
               Text(review.date.withFormat("dd/MM/yyyy"))

               val content = if (review.unitTests == null) {
                   "Testing"
               } else if (review.unitTests.errors.isEmpty()) {
                   val succeeded = review.unitTests.succeededCount
                   val total = review.unitTests.totalCount
                   "$succeeded / $total"
               } else {
                   "Error"
               }
               Text(text = content, modifier = Modifier
                   .let {
                       if (review.unitTests == null) it.background(Color.Blue)
                       if (review.unitTests!!.errors.isNotEmpty()) it.background(Color.Red)
                       else if (review.unitTests.succeededCount == review.unitTests.totalCount) it.background(
                           Color.Green
                       )
                       else it.background(Color.Yellow)
                   }
                   .border(1.dp, Color.Black)
                   .padding(20.dp)
               )
           }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(review.commentary, modifier = Modifier.padding(20.dp, 10.dp))
        }
    }
}

@Composable
fun ReviewContent(review: ReviewOneReviewDTO, modifier: Modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(review.code, modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .padding(20.dp))
        Text(review.test, modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .padding(20.dp))
    }
}

@Composable
fun CommentItem(
    comment: CommentDTO,
    navController: NavHostController
) {
    Column {
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
            Column(
                Modifier
                    .weight(1f)
                    .padding(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = comment.author.username, modifier = Modifier.clickable { navController.navigate("User/${comment.author.id}") })
                    Text(text = comment.date.withFormat("dd/MM/yyyy"))
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    Text(text = comment.content)
                }
            }
        }

        comment.responses.forEach {
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
            )

            Box(modifier = Modifier.padding(start = 30.dp)) {
                ResponseItem(it, navController)
            }
        }
    }
}

@Composable
fun ResponseItem(response: ResponseDTO, navController: NavHostController) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.KeyboardArrowUp, Modifier
                .fillMaxHeight()) {
                Log.i("test", "Poce blo")
            }
            Text("${response.likes}")
            Icon(Icons.Default.KeyboardArrowDown, Modifier
                .fillMaxHeight()) {
                Log.i("test", "Pas Poce blo")
            }
        }
        Column(
            Modifier
                .weight(1f)
                .padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = response.author.username,
                    modifier = Modifier.clickable { navController.navigate("User/${response.author.id}") })
                Text(text = response.date.withFormat("dd/MM/yyyy"))
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Text(text = response.content)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewPreview() {
    ReviewTheme {
    }
}