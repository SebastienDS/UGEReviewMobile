package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.review.CreateReviewDTO
import fr.uge.review.dto.review.ReviewCreatedDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.service.createReview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CreateReview(navController: NavHostController, apiClient: ApiClient, sessionManager: SessionManager) {
    var title by remember{ mutableStateOf("") }
    var commentary by remember{ mutableStateOf("") }
    var code by remember{ mutableStateOf("") }
    var test by remember{ mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(text = "Title")
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        singleLine = true,
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black)
                            .padding(16.dp, 8.dp)
                            .background(Color.Transparent)
                    )
                }
                Column {
                    Text(text = "Commentary")
                    BasicTextField(
                        value = commentary,
                        minLines = 2,
                        maxLines = 3,
                        onValueChange = { commentary = it },
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black)
                            .padding(16.dp, 8.dp)
                            .background(Color.Transparent)
                    )
                }

                Column {
                    Text(text = "Code")
                    BasicTextField(
                        value = code,
                        onValueChange = { code = it },
                        minLines = 5,
                        maxLines = 5,
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black)
                            .padding(16.dp, 8.dp)
                            .background(Color.Transparent)
                    )
                }
                Column {
                    Text(text = "Test")
                    BasicTextField(
                        value = test,
                        onValueChange = { test = it },
                        minLines = 5,
                        maxLines = 5,
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black)
                            .padding(16.dp, 8.dp)
                            .background(Color.Transparent)
                    )
                }
                Text("Create Review", modifier = Modifier
                    .border(1.dp, Color.Black)
                    .padding(16.dp, 8.dp)
                    .clickable {
                        val review = CreateReviewDTO(title, commentary, code, test)
                        handleCall(apiClient.reviewService.createReview(review), onFailure = {
                            title = ""
                            commentary = ""
                            code = ""
                            test = ""
                        }) {
                            navController.navigate("Review/${it.id}")
                        }
                    })
            }
        }
        Footer(navController, sessionManager = sessionManager,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )
    }
}