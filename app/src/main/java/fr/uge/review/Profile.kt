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
        handleCall(apiClient.userService.userProfile(userId), onFailure = {
            Log.i("UwU", "User $userId not found")
            navController.navigate("NotFound")
        }) {
            userProfile = it
        }
        handleCall(apiClient.userService.fetchFollowState(userId)) {
            isFollowing = it.isUserFollowing
        }
    }
    Column {
        if(sessionManager.isAuthenticated() && sessionManager.getUserId() != userId) {
            Button(
                onClick = {
                    if (isFollowing) {
                        handleCall(apiClient.userService.unfollowUser(userId)) {
                            isFollowing = !isFollowing
                        }
                    } else {
                        handleCall(apiClient.userService.followUser(userId)) {
                            isFollowing = !isFollowing
                        }
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
                    text = userProfile!!.username,
                    style = TextStyle(fontSize = 18.sp)
                )
                if (sessionManager.isAuthenticated() && sessionManager.getUserId() == userId) {
                    Column {
                        Text(
                            text = userProfile!!.email,
                            style = TextStyle(fontSize = 18.sp)
                        )
                        Text(
                            text = userProfile!!.dateCreation.withFormat("dd/MM/yyyy"),
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
                        handleCall(apiClient.userService.logout()) {
                            sessionManager.clear()
                            navController.navigate("Connection")
                        }
                    }){
                Text("Se déconnecter")
            }
            if (sessionManager.isAuthenticated() && sessionManager.getUserId() == userId) {
                Box(modifier = Modifier
                    .clickable {
                        handleCall(apiClient.userService.deleteProfile()) {
                            sessionManager.clear()
                            navController.navigate("Connection")
                        }
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
