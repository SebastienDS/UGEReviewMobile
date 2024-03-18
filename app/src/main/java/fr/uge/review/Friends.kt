package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.review.dto.response.ResponseUserDTO
import fr.uge.review.dto.user.UserDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme
import retrofit2.Call
import retrofit2.Callback
fun fetchUserFriends(userId: Long, page: Int, apiClient: ApiClient, onSuccess: (List<UserDTO>) -> Unit, onFailure: (Throwable?) -> Unit) {
    apiClient.userService.fetchUserFriends(userId, page, 20)
        .enqueue(object : Callback<List<UserDTO>> {
            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                Log.e("UwU",  "OwO review", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<List<UserDTO>>, response: retrofit2.Response<List<UserDTO>>) {
                if (response.isSuccessful) {
                    val responseDTO = response.body()!!
                    Log.i("UwU", responseDTO.toString())
                    onSuccess(responseDTO)
                } else {
                    Log.e("UwU", "OwO Review FAIL")
                    onFailure(null)
                }
            }
        })
}

@Composable
fun Friends(
    navController: NavHostController,
    userId: Long,
    sessionManager: SessionManager,
    apiClient: ApiClient
) {
    var friends: List<UserDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }
    fetchUserFriends(userId, page, apiClient, {friends = it}, {})

    Column {
        FriendsViewer(navController, friends, modifier = Modifier
            .weight(1f)
            .background(Color.White)
            .fillMaxWidth(),
            previous = {
                page--
            },
            next = {
                page++
            })
        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun FriendsViewer(navController: NavHostController, friends: List<UserDTO>?, modifier: Modifier, previous: () -> Unit, next: () -> Unit ) {
    if(friends == null) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Refresh, Modifier.size(100.dp)) {
                Log.i("UwU", "Refresh")
            }
        }
    }else{
        LazyColumn(modifier = modifier) {
            items(friends) {
                FriendRow(navController,
                    it,
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp))
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Button(
                onClick = previous,
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = "Précedent")
            }
            Button(
                onClick = next,
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = "Suivant")
            }
        }
    }
}

@Composable
fun FriendRow(navController: NavHostController, friend: UserDTO, modifier: Modifier) {
    Row(modifier) {
        FriendItem(navController, friend, Modifier.weight(1f))
        Box(Modifier.width(100.dp), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Delete, Modifier
                .fillMaxHeight()
                .padding(4.dp)) {
                Log.i("test", "Delete $friend")
            }
        }
    }
}

@Composable
fun FriendItem(navController: NavHostController, friend: UserDTO, modifier: Modifier) {
    Box(modifier.clickable { navController.navigate("Users/${friend.id}") }) {
        Text(friend.username, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun FriendsPreview() {
    ReviewTheme {
    }
}