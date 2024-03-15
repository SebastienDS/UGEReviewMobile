package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserLoginDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                    .size(300.dp, 60.dp)
                    .padding(16.dp, 8.dp)
                    .border(1.dp, Color.Black)
                    .background(Color.Transparent)
            )
            Column (Modifier.padding(bottom = 20.dp, top = 20.dp)) {
                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                    modifier = Modifier
                        .size(300.dp, 60.dp)
                        .padding(16.dp, 8.dp)
                        .border(1.dp, Color.Black)
                        .background(Color.Transparent)
                )
                Text("Mot de passe oublié", color = Color.Gray, modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { /*TODO: navigate when page done*/})

                Text("Créer un compte", color = Color.Gray, modifier = Modifier
                    .padding(start = 16.dp, top = 5.dp)
                    .clickable { navController.navigate("Signup") })
            }
            Text("Connect", modifier = Modifier
                .padding(start = 180.dp)
                .border(1.dp, Color.Black)
                .padding(20.dp, 15.dp)
                .clickable {
                    trylogin(apiClient, UserLoginDTO(username, password), sessionManager,
                        onSuccess = {
                            navController.navigate("Profile/${sessionManager.getUserId()}")
                        }, onFailure = {
                            username = ""
                            password = ""
                        })
                })
        }
        Footer(navController, sessionManager = sessionManager,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )
    }
}


fun trylogin(apiClient: ApiClient, userLoginDTO: UserLoginDTO, sessionManager: SessionManager,
             onSuccess: () -> Unit, onFailure: () -> Unit)  {
    apiClient.userService.login(userLoginDTO)
        .enqueue(object : Callback<UserDataDTO> {
            override fun onFailure(call: Call<UserDataDTO>, t: Throwable) {
                // Error logging in
                Log.e("UwU",  "OwO login", t)
            }

            override fun onResponse(call: Call<UserDataDTO>, response: Response<UserDataDTO>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.i("UwU", data.toString())
                    sessionManager.setToken(userLoginDTO.username, userLoginDTO.password)
                    sessionManager.setUserData(data!!)
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO LOGIN FAIL")
                    onFailure()
                }
            }
        })
}

@Preview(showBackground = true)
@Composable
fun ConnectionPreview() {
    ReviewTheme {
    }
}
