package fr.uge.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.resetPassword.AskEmailDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme

enum class State {
    SUCCESS,
    FAILED,
    DEFAULT
}

@Composable
fun ResetPassword(
    navController: NavHostController,
    apiClient: ApiClient,
    sessionManager: SessionManager
) {
    var email by remember{ mutableStateOf("")}
    var state by remember {
        mutableStateOf(State.DEFAULT)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Column (Modifier.padding(bottom = 20.dp, top = 20.dp)
            .fillMaxSize()
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp, 8.dp)
                    .border(1.dp, Color.Black)
                    .padding(16.dp, 8.dp)
                    .background(Color.Transparent)
            )
            Text(
                stringResource(id = R.string.send), modifier = Modifier
                    .padding(start = 180.dp)
                    .border(1.dp, Color.Black)
                    .padding(20.dp, 15.dp)
                    .clickable {
                        handleCall(
                            apiClient.resetPasswordService.resetPassword(AskEmailDTO(email)),
                            onFailure = {
                                email = ""
                                state = State.FAILED
                            }) {
                            state = State.SUCCESS
                        }
                    })
            if (state == State.SUCCESS) {
                Text(
                    stringResource(id = R.string.emailSend), modifier = Modifier
                        .padding(20.dp, 15.dp)
                        .border(1.dp, Color.Black)
                        .padding(20.dp, 15.dp))

            }
            else if (state == State.FAILED) {
                Text(
                    stringResource(id = R.string.emailNotSend), modifier = Modifier
                        .padding(20.dp, 15.dp)
                        .border(1.dp, Color.Black)
                        .padding(20.dp, 15.dp))

            }
        }

        Footer(navController, sessionManager = sessionManager,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordPreview() {
    ReviewTheme {
    }
}
