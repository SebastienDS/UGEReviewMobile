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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme

data class Friend(val name: String)

@Composable
fun Friends(navController: NavHostController, sessionManager: SessionManager) {
    val friends = (1..100).map { Friend("Friend $it") }

    Column {
        FriendsViewer(navController, friends, modifier = Modifier
            .weight(1f)
            .background(Color.White)
            .fillMaxWidth())
        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun FriendsViewer(navController: NavHostController, friends: List<Friend>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(friends) {
            FriendRow(navController,
                it,
                Modifier
                    .fillMaxWidth()
                    .height(25.dp))
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

@Composable
fun FriendRow(navController: NavHostController, friend: Friend, modifier: Modifier) {
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
fun FriendItem(navController: NavHostController, friend: Friend, modifier: Modifier) {
    Box(modifier.clickable { navController.navigate("Profile/0") }) {
        Text(friend.name, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun FriendsPreview() {
    ReviewTheme {
    }
}