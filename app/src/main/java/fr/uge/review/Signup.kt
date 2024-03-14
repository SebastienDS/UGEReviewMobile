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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.user.UserLoginDTO
import fr.uge.review.dto.user.UserSignUpDTO
import fr.uge.review.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Signup(navController: NavHostController, apiClient: ApiClient, sessionManager: SessionManager) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, Color.Black)
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .background(Color.Transparent)
            )

            BasicTextField(
                value = email,
                onValueChange = { email = it },
                textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                modifier = Modifier
                    .size(300.dp, 60.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, Color.Black)
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .background(Color.Transparent)
            )

            BasicTextField(
                value = password,
                onValueChange = { password = it },
                textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                modifier = Modifier
                    .size(300.dp, 60.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, Color.Black)
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .background(Color.Transparent)
            )
            Text("Créer le compte", modifier = Modifier
                .padding(start = 180.dp)
                .border(1.dp, Color.Black)
                .padding(20.dp, 15.dp)
                .clickable {
                    trySignup(apiClient, UserSignUpDTO(username, email, password),
                        onSuccess = {
                            navController.navigate("Connection")
                        }, onFailure = {
                            username = ""
                            email = ""
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


fun trySignup(apiClient: ApiClient, userSignUpDTO: UserSignUpDTO,
             onSuccess: () -> Unit, onFailure: () -> Unit)  {
    apiClient.userService.signup(userSignUpDTO)
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Error logging in
                Log.e("UwU",  "OwO signup", t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.e("UwU",  "UwU SIGNUP SUCCESS")
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO SIGNUP FAIL")
                    onFailure()
                }
            }
        })
}