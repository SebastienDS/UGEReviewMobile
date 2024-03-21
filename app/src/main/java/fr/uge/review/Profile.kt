package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.user.UserFollowStateDTO
import fr.uge.review.dto.user.UserProfileDTO
import fr.uge.review.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Menu(navController: NavHostController, userId: Long, modifier: Modifier) {
    Column(
        modifier
            .background(Color.White)
            .verticalScroll(state = rememberScrollState())){
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clickable { navController.navigate("Users/$userId/reviews") }){
            Text("Revue")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clickable { navController.navigate("Users/$userId/comments") }){
            Text("Commentaire")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clickable { navController.navigate("Users/$userId/responses") }){
            Text("Réponse")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clickable { navController.navigate("Users/$userId/friends") }){
            Text("Amis")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clickable { navController.navigate("Users/$userId/likes") }){
            Text("Likes")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
    }
}

@Composable
fun Profile(
    navController: NavHostController,
    userId: Long,
    apiClient: ApiClient,
    sessionManager: SessionManager
) {
    if (!sessionManager.isAuthenticated()) {
        navController.navigate("Connection")
    }
    var userProfile: UserProfileDTO? by remember { mutableStateOf(null) }
    var isFollowing by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        userProfile(userId, apiClient, { userProfile = it }, {
            Log.i("", "User $userId not found")
            navController.navigate("NotFound")
        })
        fetchFollowState(userId, apiClient, {isFollowing = it}) {}
    }
    Column {
        if(sessionManager.isAuthenticated() && sessionManager.getUserId() != userId) {
            Button(
                onClick = {
                    if (isFollowing) {
                        unfollowUser(userId, apiClient, {isFollowing = !isFollowing}) {}
                    } else {
                        followUser(userId, apiClient, sessionManager, {isFollowing = !isFollowing}, {})
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = if (isFollowing) "Unfollow" else "Follow")
            }
        }

        Menu(navController, userId, modifier = Modifier
            .weight(1f)
            .fillMaxWidth())

        if (userProfile != null) {
            Column(modifier = Modifier
                .fillMaxWidth()) {
                Text(
                    text = "${userProfile!!.username}",
                    style = TextStyle(fontSize = 18.sp)
                )
                if (sessionManager.isAuthenticated() && sessionManager.getUserId() == userId) {
                    Column {
                        Text(
                            text = "${userProfile!!.email}",
                            style = TextStyle(fontSize = 18.sp)
                        )
                        Text(
                            text = "${userProfile!!.dateCreation}",
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                    .clickable {
                        logout(apiClient = apiClient, sessionManager = sessionManager,
                            onSuccess = {
                                navController.navigate("Connection")
                            }, onFailure = {})
                    }){
                Text("Se déconnecter")
            }
            if (sessionManager.isAuthenticated() && sessionManager.getUserId() == userId) {
                Box(modifier = Modifier
                    .clickable {
                        deleteProfile(apiClient = apiClient, sessionManager = sessionManager,
                            onSuccess = {
                                navController.navigate("Connection")
                            }, onFailure = {})
                    }){
                    Text("Supprimer mon compte")
                }
            }
        }

        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

fun logout(apiClient: ApiClient, sessionManager: SessionManager,
             onSuccess: () -> Unit, onFailure: (Throwable?) -> Unit)  {
    apiClient.userService.logout()
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO logout", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU LOGOUT SUCCESS")
                    sessionManager.clear()
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO LOGOUT FAIL")
                    onFailure(null)
                }
            }
        })
}

fun deleteProfile(apiClient: ApiClient, sessionManager: SessionManager,
           onSuccess: () -> Unit, onFailure: (Throwable?) -> Unit)  {
    apiClient.userService.deleteProfile()
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO deleteProfile", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU deleteProfile SUCCESS")
                    sessionManager.clear()
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO deleteProfile FAIL")
                    onFailure(null)
                }
            }
        })
}

fun userProfile(userId: Long, apiClient: ApiClient, onSuccess: (UserProfileDTO) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.userService.userProfile(userId)
        .enqueue(object : Callback<UserProfileDTO> {
            override fun onFailure(call: Call<UserProfileDTO>, t: Throwable) {
                Log.e("UwU",  "OwO userProfile", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<UserProfileDTO>, response: retrofit2.Response<UserProfileDTO>) {
                if (response.isSuccessful) {
                    val users = response.body()!!
                    Log.i("UwU", users.toString())
                    onSuccess(users)
                } else {
                    Log.e("UwU", "OwO userProfile FAIL")
                    onFailure(null)
                }
            }
        })
}

fun followUser(userId: Long, apiClient: ApiClient, sessionManager: SessionManager,
                  onSuccess: () -> Unit, onFailure: (Throwable?) -> Unit)  {
    apiClient.userService.followUser(userId)
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO followUser", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU followUser SUCCESS")
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO followUser FAIL")
                    onFailure(null)
                }
            }
        })
}

fun unfollowUser(
    userId: Long, apiClient: ApiClient, onSuccess: () -> Unit,
    onFailure: (Throwable?) -> Unit
)  {
    apiClient.userService.unfollowUser(userId)
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO unfollowUser", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU unfollowUser SUCCESS")
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO unfollowUser FAIL")
                    onFailure(null)
                }
            }
        })
}

fun fetchFollowState(
    userId: Long,
    apiClient: ApiClient,
    onSuccess: (Boolean) -> Unit,
    onFailure: (Throwable?) -> Unit
)  {
    apiClient.userService.fetchFollowState(userId)
        .enqueue(object : Callback<UserFollowStateDTO> {
            override fun onFailure(call: Call<UserFollowStateDTO>, t: Throwable) {
                Log.e("UwU",  "OwO fetchFollowState", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<UserFollowStateDTO>, response: Response<UserFollowStateDTO>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU fetchFollowState SUCCESS")
                    onSuccess(response.body()!!.state)

                } else {
                    Log.e("UwU", "OwO fetchFollowState FAIL")
                    onFailure(null)
                }
            }
        })
}

