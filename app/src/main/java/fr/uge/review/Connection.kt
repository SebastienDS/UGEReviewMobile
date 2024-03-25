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
import fr.uge.review.dto.user.UserLoginDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme

@Composable
fun Connection(
    navController: NavHostController,
    apiClient: ApiClient,
    sessionManager: SessionManager
) {
    var username by remember{ mutableStateOf("")}
    var password by remember{ mutableStateOf("")}

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                value = username,
                onValueChange = { username = it },
                textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp, 8.dp)
                    .border(1.dp, Color.Black)
                    .padding(16.dp, 8.dp)
                    .background(Color.Transparent)
            )
            Column (Modifier.padding(bottom = 20.dp, top = 20.dp)) {
                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                    modifier = Modifier
                        .width(300.dp)
                        .padding(16.dp, 8.dp)
                        .border(1.dp, Color.Black)
                        .padding(16.dp, 8.dp)
                        .background(Color.Transparent)
                )
                Text(stringResource(id = R.string.forgetPassword), color = Color.Gray, modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { /*TODO: navigate when page done*/})

                Text(stringResource(id = R.string.signUp), color = Color.Gray, modifier = Modifier
                    .padding(start = 16.dp, top = 5.dp)
                    .clickable { navController.navigate("Signup") })
            }
            Text(
                stringResource(id = R.string.connect), modifier = Modifier
                .padding(start = 180.dp)
                .border(1.dp, Color.Black)
                .padding(20.dp, 15.dp)
                .clickable {
                    handleCall(apiClient.userService.login(UserLoginDTO(username, password)), onFailure = {
                        username = ""
                        password = ""
                    }) {
                        sessionManager.setToken(username, password)
                        sessionManager.setUserData(it)
                        navController.navigate("Users/${sessionManager.getUserId()}")
                    }
                })
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
fun ConnectionPreview() {
    ReviewTheme {
    }
}
