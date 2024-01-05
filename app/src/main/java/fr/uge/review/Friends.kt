package fr.uge.review

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.review.ui.theme.ReviewTheme

data class Friend(val name: String)

@Composable
fun Friends() {
    val friends = (1..100).map { Friend("Friend $it") }

    Column {
        FriendsViewer(friends, modifier = Modifier
            .weight(1f)
            .fillMaxWidth())
        Footer(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun FriendsViewer(friends: List<Friend>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(friends) {
            FriendRow(it, Modifier.fillMaxWidth().height(25.dp))
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
fun FriendRow(friend: Friend, modifier: Modifier) {
    Row(modifier) {
        FriendItem(friend, Modifier.weight(1f))
        Box(Modifier.width(150.dp), contentAlignment = Alignment.Center) {
            IconButton(
                onClick = {
                    Log.i("test", "Delete $friend")
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun FriendItem(friend: Friend, modifier: Modifier) {
    Box(modifier) {
        Text(friend.name)
    }
}

@Preview(showBackground = true)
@Composable
fun FriendsPreview() {
    ReviewTheme {
        Friends()
    }
}