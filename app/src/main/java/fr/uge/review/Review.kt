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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.comment.CommentDTO
import fr.uge.review.dto.like.LikeState
import fr.uge.review.dto.like.LikeStateDTO
import fr.uge.review.dto.response.ResponseDTO
import fr.uge.review.dto.response.SendResponseDTO
import fr.uge.review.dto.review.ReviewOneReviewDTO
import fr.uge.review.dto.user.Role
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.Callback

fun fetchReview(reviewId: Long, apiClient: ApiClient, onSuccess: (ReviewOneReviewDTO) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.reviewService.fetchReview(reviewId)
        .enqueue(object : Callback<ReviewOneReviewDTO> {
            override fun onFailure(call: Call<ReviewOneReviewDTO>, t: Throwable) {
                Log.e("Failed",  "Review fetch FAIL", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<ReviewOneReviewDTO>, response: retrofit2.Response<ReviewOneReviewDTO>) {
                if (response.isSuccessful) {
                    val review = response.body()!!
                    Log.i("Success", "Review fetch")
                    onSuccess(review)
                } else {
                    Log.e("Failed", "Review fetch FAIL")
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
                    Log.i("Success", "Delete Review")
                } else {
                    Log.e("UwU", "OwO Review FAIL")
                }
            }
        })
}

fun createComment(reviewId: Long, content: String, apiClient: ApiClient, onSuccess: (CommentDTO) -> Unit) {
    Log.i("OwO", content.length.toString())
    if(content == ""){
        return
    }
    apiClient.commentService.createComment(reviewId,  content)
        .enqueue(object : Callback<CommentDTO> {
            override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
                Log.e("Failed",  "Comment Failed", t)
            }

            override fun onResponse(call: Call<CommentDTO>, response: retrofit2.Response<CommentDTO>) {
                if (response.isSuccessful) {
                    val comment = response.body()!!
                    onSuccess(comment)
                    Log.i("Success", "Comment created")
                } else {
                    Log.e("Failed", "Comment Failed")
                }
            }
        })
}

fun createResponse(reviewId: Long, commentId: Long, content: String, apiClient: ApiClient, onSuccess: (ResponseDTO) -> Unit) {
    if(content == ""){
        return
    }
    Log.i("Information", "$reviewId $commentId $content")
    apiClient.responseService.createResponse(reviewId, SendResponseDTO(commentId,  content))
        .enqueue(object : Callback<ResponseDTO> {
            override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                Log.e("Failed",  "Response Failed", t)
            }

            override fun onResponse(call: Call<ResponseDTO>, response: retrofit2.Response<ResponseDTO>) {
                if (response.isSuccessful) {
                    val responseDTO = response.body()!!
                    onSuccess(responseDTO)
                    Log.i("Success", "Response created")
                } else {
                    Log.e("Failed", "Response Failed")
                }
            }
        })
}


fun like(sessionManager: SessionManager,
                navController: NavHostController,  call : Call<LikeStateDTO>, onSuccess: (LikeStateDTO) -> Unit) {
    checkConnection(sessionManager.isAuthenticated(), navController)
    call
        .enqueue(object : Callback<LikeStateDTO> {
            override fun onFailure(call: Call<LikeStateDTO>, t: Throwable) {
                Log.e("Failed",  "Comment Not Liked", t)
            }

            override fun onResponse(call: Call<LikeStateDTO>, response: retrofit2.Response<LikeStateDTO>) {
                if (response.isSuccessful) {
                    val likeStateDTO = response.body()!!
                    onSuccess(likeStateDTO)
                    Log.i("Success", "Comment Liked")
                } else {
                    Log.e("Failed", "Comment Not Liked")
                }
            }
        })
}

fun checkConnection(authenticated: Boolean, navController: NavHostController) {
    if(!authenticated){
        navController.navigate("Connection")
    }
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
            ReviewViewer(apiClient, sessionManager, navController, review!!, modifier = modifier)
        }

        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun ReviewViewer(apiClient: ApiClient, sessionManager: SessionManager,
                 navController: NavHostController, review: ReviewOneReviewDTO, modifier: Modifier) {
    val (comments, setComments) = remember { mutableStateOf(review.comments) }
    LazyColumn(modifier) {
        item {
            ReviewHeader(sessionManager, apiClient, navController, review)
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
        items(comments) {
            CommentItem(review.id, it, apiClient, sessionManager, navController)
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
        item{
            AddComment(review.id, apiClient){setComments(comments + it)}
        }
    }
}

@Composable
fun AddComment(
    reviewId: Long,
    apiClient: ApiClient,
    addComment: (CommentDTO) -> Unit
) {
    var content by remember{ mutableStateOf("") }
    BasicTextField(
        value = content,
        onValueChange = { content = it },
        minLines = 5,
        maxLines = 5,
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .padding(16.dp, 8.dp)
            .background(Color.Transparent))
    Button(onClick = {createComment(reviewId, content, apiClient) {
        content = ""
        addComment.invoke(it)
    } }) {
        Text("Commenter")
    }
}

fun computeCommentsCount(review: ReviewOneReviewDTO): Int =
    review.comments.sumOf { it.responses.size + 1 }

@Composable
fun ReviewHeader(sessionManager: SessionManager, apiClient: ApiClient, navController: NavHostController, review: ReviewOneReviewDTO) {
    Column(Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(review.title, fontSize = 30.sp)
        }
        Row(Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceAround) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(5.dp)) {
                var like by remember { mutableIntStateOf(review.likes) }
                var likeStateRemember by remember { mutableStateOf(review.likeState) }
                IconLike(likeStateRemember, LikeState.LIKE,1f){
                    like(sessionManager, navController, apiClient.reviewService.likeReviews(review.id)){
                        like = it.likes;
                        likeStateRemember = it.likeState
                    }
                }
                Text("$like")
                IconLike(likeStateRemember, LikeState.DISLIKE,-1f){
                    like(sessionManager, navController, apiClient.reviewService.disLikeReviews(review.id)){
                        like = it.likes;
                        likeStateRemember = it.likeState
                    }
                }
            }

            Column(modifier = Modifier
                .weight(1f)
                .padding(5.dp), verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.End) {
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
    reviewId: Long,
    comment: CommentDTO,
    apiClient: ApiClient,
    sessionManager: SessionManager,
    navController: NavHostController
) {
    var like by remember { mutableIntStateOf(comment.likes) }
    var likeStateRemember by remember { mutableStateOf(comment.likeState) }
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                IconLike(likeStateRemember, LikeState.LIKE, 1f){
                    like(sessionManager, navController, apiClient.commentService.likeComment(comment.id)){
                        like = it.likes;
                        likeStateRemember = it.likeState
                    }
                }
                Text("$like")
                IconLike(likeStateRemember, LikeState.DISLIKE,-1f){
                    like(sessionManager, navController, apiClient.commentService.dislikeComment(comment.id)){
                        like = it.likes;
                        likeStateRemember = it.likeState
                    }
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
        val (responses, setResponses) = remember { mutableStateOf(comment.responses) }

        responses.forEach {
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
            )

            Box(modifier = Modifier.padding(start = 30.dp)) {
                ResponseItem(it, apiClient, sessionManager, navController)
            }
        }

        var isResponding by remember { mutableStateOf(false) }
        if(isResponding){
            AddResponse(reviewId, comment.id, apiClient){
                isResponding = false
                setResponses(responses + it)
            }
        }else{
            Button(onClick = {isResponding = true}) {
                Text("Répondre")
            }
        }
    }
}

@Composable
fun AddResponse(
    reviewId: Long,
    commentId: Long,
    apiClient: ApiClient,
    onSuccess: (ResponseDTO) -> Unit
) {
    var content by remember{ mutableStateOf("") }
    BasicTextField(
        value = content,
        onValueChange = { content = it },
        minLines = 5,
        maxLines = 5,
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .padding(16.dp, 8.dp)
            .background(Color.Transparent))
    Button(onClick = {createResponse(reviewId, commentId, content, apiClient) {
        content = ""
        onSuccess.invoke(it)
    } }) {
        Text("Commenter")
    }
}

@Composable
fun IconLike(likeState: LikeState, likeStateDefault: LikeState, scaleYIcon: Float, onClick: () -> Unit) {
    var icon = Icons.Outlined.ThumbUp;
    if (likeState == likeStateDefault) {
        icon = Icons.Default.ThumbUp
    }
    Icon(icon,
        Modifier
            .graphicsLayer { scaleY = scaleYIcon }
            .fillMaxHeight(),
        onClick)
}

@Composable
fun ResponseItem(response: ResponseDTO,
                 apiClient: ApiClient,
                 sessionManager: SessionManager,
                 navController: NavHostController) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            var like by remember { mutableIntStateOf(response.likes) }
            var likeStateRemember by remember { mutableStateOf(response.likeState) }
            IconLike(likeStateRemember, LikeState.LIKE,1f){
                like(sessionManager, navController, apiClient.responseService.likeResponses(response.id)){
                    like = it.likes;
                    likeStateRemember = it.likeState
                }
            }
            Text("$like")
            IconLike(likeStateRemember, LikeState.DISLIKE,-1f){
                like(sessionManager, navController, apiClient.responseService.dislikeResponses(response.id)){
                    like = it.likes;
                    likeStateRemember = it.likeState
                }
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