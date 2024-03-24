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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.ui.res.stringResource
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
import retrofit2.Call


fun like(sessionManager: SessionManager,
                navController: NavHostController,  call : Call<LikeStateDTO>, onSuccess: (LikeStateDTO) -> Unit) {
    checkConnection(sessionManager.isAuthenticated(), navController)
    handleCall(call) {
        onSuccess(it)
    }
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
    var isRequestingNotification: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(reviewId) {
        handleCall(apiClient.reviewService.fetchReview(reviewId)) {
            review = it
        }
    }
    LaunchedEffect(Unit) {
        handleCall(apiClient.notificationService.fetchNotificationState(reviewId)) {
            isRequestingNotification = it.isUserRequestingNotification
        }
    }

    Column {
        val role = sessionManager.getUserRole()
        if(role == Role.ADMIN) {
            Button(onClick = {
                handleCall(apiClient.reviewService.deleteReview(reviewId)) {
                    navController.navigate("Home")
                }
            }) {
                Text(text = stringResource(id = R.string.delete))
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
            ReviewViewer(navController, review!!, isRequestingNotification, sessionManager, apiClient, modifier = modifier) {
                if (isRequestingNotification) {
                    handleCall(apiClient.notificationService.deactivateNotification(reviewId)) {
                        isRequestingNotification = false
                    }
                } else {
                    handleCall(apiClient.notificationService.activateNotification(reviewId)) {
                        isRequestingNotification = true
                    }
                }
            }
        }

        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun ReviewViewer(
    navController: NavHostController,
    review: ReviewOneReviewDTO,
    isRequestingNotification: Boolean,
    sessionManager: SessionManager,
    apiClient: ApiClient,
    modifier: Modifier,
    onNotificationButtonClick: () -> Unit
) {
    val role = sessionManager.getUserRole()
    var (comments, setComments) = remember { mutableStateOf(review.comments) }

    LazyColumn(modifier) {
        item {
            ReviewHeader(navController, review, isRequestingNotification, onNotificationButtonClick, sessionManager, apiClient)
            ReviewContent(review, modifier = Modifier.padding(20.dp, 10.dp))

            val count = computeCommentsCount(review)
            Text("$count ${stringResource(id = R.string.create)}:", Modifier.padding(3.dp))

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
        items(comments) {
            if(role == Role.ADMIN) {
                Button(onClick = {
                    handleCall(apiClient.commentService.deleteComment(review.id, it.id)) {
                        setComments(comments.filter { comment -> comment.id != it.id})
                    }
                }) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }
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
    Button(onClick = {
        if (content != "") {
            handleCall(apiClient.commentService.createComment(reviewId, content)) {
                content = ""
                addComment(it)
            }
        }
    }) {
        Text(stringResource(id = R.string.comment))
    }
}

fun computeCommentsCount(review: ReviewOneReviewDTO): Int =
    review.comments.sumOf { it.responses.size + 1 }

@Composable
fun ReviewHeader(
    navController: NavHostController,
    review: ReviewOneReviewDTO,
    isRequestingNotification: Boolean,
    onNotificationButtonClick: () -> Unit,
    sessionManager: SessionManager,
    apiClient: ApiClient,
) {
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
                Text(review.author.username, Modifier.clickable { navController.navigate("Users/${review.author.id}") })
                Text(review.date.withFormat(stringResource(id = R.string.date)))

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

                if (sessionManager.isAuthenticated()) {
                    val icon = if (isRequestingNotification) Icons.Outlined.Notifications else Icons.Default.Notifications
                    Icon(icon, Modifier.size(50.dp)) {
                        onNotificationButtonClick()
                    }
                }
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
                    Text(text = comment.author.username, modifier = Modifier.clickable { navController.navigate("Users/${comment.author.id}") })
                    Text(text = comment.date.withFormat(stringResource(id = R.string.date)))
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
                Text(stringResource(id = R.string.respond))
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
    Button(onClick = {
        handleCall(apiClient.responseService.createResponse(reviewId, SendResponseDTO(commentId, content))) {
            content = ""
            onSuccess(it)
        }
    }) {
        Text(stringResource(id = R.string.respond))
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
                    modifier = Modifier.clickable { navController.navigate("Users/${response.author.id}") })
                Text(text = response.date.withFormat(stringResource(id = R.string.date)))
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