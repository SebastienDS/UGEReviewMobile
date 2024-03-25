package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.updatePassword.UpdatePasswordDTO
import fr.uge.review.dto.user.Role
import fr.uge.review.dto.user.UserProfileDTO
import fr.uge.review.service.SessionManager

@Composable
fun Menu(navController: NavHostController, userId: Long, modifier: Modifier) {
    Column(modifier.background(Color.White)) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clickable { navController.navigate("Users/$userId/reviews") }){
            Text(stringResource(id = R.string.review))
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
            Text(stringResource(id = R.string.comments))
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
            Text(stringResource(id = R.string.responses))
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
            Text(stringResource(id = R.string.friends))
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
            Text(stringResource(id = R.string.like))
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
    if (sessionManager.getUserId() == userId && !sessionManager.isAuthenticated()) {
        navController.navigate("Connection")
    }
    var userProfile: UserProfileDTO? by remember { mutableStateOf(null) }
    var isFollowing by remember { mutableStateOf(false) }
    val role = sessionManager.getUserRole()


    LaunchedEffect(Unit) {
        handleCall(apiClient.userService.userProfile(userId)) {
            userProfile = it
        }
        handleCall(apiClient.userService.fetchFollowState(userId)) {
            isFollowing = it.isUserFollowing
        }
    }
    Column {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())) {
            if (sessionManager.isAuthenticated() && sessionManager.getUserId() != userId) {
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

            Menu(navController, userId, modifier = Modifier.fillMaxWidth())

            if (userProfile != null) {
                if (sessionManager.isAuthenticated() && sessionManager.getUserId() == userId) {
                    ModifiableField(userProfile!!.username) {
                        handleCall(apiClient.userService.updateUsername(userId, it)) {
                            userProfile = userProfile!!.copy(username = it)
                            Log.i("UwU", sessionManager.getAuthToken().toString())
                            sessionManager.updateUsername(it)
                            Log.i("UwU", sessionManager.getAuthToken().toString())
                        }
                    }
                } else {
                    Text(
                        text = userProfile!!.username,
                        style = TextStyle(fontSize = 18.sp)
                    )
                }
                if (sessionManager.isAuthenticated() && sessionManager.getUserId() == userId) {
                    ModifiableField(userProfile!!.email) {
                        handleCall(apiClient.userService.updateEmail(userId, it)) {
                            userProfile = userProfile!!.copy(email = it)
                        }
                    }
                    Text(
                        text = userProfile!!.dateCreation.withFormat("dd/MM/yyyy"),
                        style = TextStyle(fontSize = 18.sp)
                    )
                    PasswordUpdate("********") { oldPassword, newPassword ->
                        handleCall(apiClient.userService.updatePassword(userId, UpdatePasswordDTO(oldPassword, newPassword))) {
                            sessionManager.updatePassword(newPassword)
                        }
                    }
                }
            }

            if (sessionManager.isAuthenticated()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .clickable {
                            handleCall(apiClient.userService.logout()) {
                                sessionManager.clear()
                                navController.navigate("Connection")
                            }
                        }) {
                        Text(stringResource(id = R.string.disconnect))
                    }
                    if (sessionManager.getUserId() == userId) {
                        Box(modifier = Modifier
                            .clickable {
                                handleCall(apiClient.userService.deleteProfile()) {
                                    sessionManager.clear()
                                    navController.navigate("Connection")
                                }
                            }) {
                            Text(stringResource(id = R.string.deleteAccount))
                        }
                    }
                    if (role == Role.ADMIN && sessionManager.getUserId() != userId) {
                        Box(modifier = Modifier
                            .clickable {
                                handleCall(apiClient.userService.banProfile(userId)) {
                                    navController.navigate("Home")
                                }
                            }) {
                            Text(stringResource(id = R.string.banAccount))
                        }
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
fun ModifiableField(value: String, onUpdate: (String) -> Unit) {
    var updatedValue by remember { mutableStateOf(value) }
    var updating by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (updating) {
            BasicTextField(
                value = updatedValue,
                onValueChange = { updatedValue = it },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp, color = Color.White),
                modifier = Modifier
                    .border(1.dp, Color.White)
                    .padding(8.dp, 4.dp)
                    .background(Color.Transparent)
            )
            Icon(imageVector = Icons.Default.Check, modifier = Modifier.size(50.dp)) {
                onUpdate(updatedValue)
                updating = false
            }

        } else {
            Text(
                text = updatedValue,
                style = TextStyle(fontSize = 18.sp)
            )
            Icon(imageVector = Icons.Default.Edit, modifier = Modifier.size(50.dp)) {
                updating = true
            }
        }


    }
}

@Composable
fun PasswordUpdate(value: String, onUpdate: (String, String) -> Unit) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var updating by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (updating) {
            Column {
                BasicTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp, color = Color.White),
                    modifier = Modifier
                        .border(1.dp, Color.White)
                        .padding(8.dp, 4.dp)
                        .background(Color.Transparent)
                )
                BasicTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp, color = Color.White),
                    modifier = Modifier
                        .border(1.dp, Color.White)
                        .padding(8.dp, 4.dp)
                        .background(Color.Transparent)
                )
            }
            Icon(imageVector = Icons.Default.Check, modifier = Modifier.size(50.dp)) {
                onUpdate(oldPassword, newPassword)
                updating = false
            }

        } else {
            Text(
                text = value,
                style = TextStyle(fontSize = 18.sp)
            )
            Icon(imageVector = Icons.Default.Edit, modifier = Modifier.size(50.dp)) {
                updating = true
            }
        }
    }
}