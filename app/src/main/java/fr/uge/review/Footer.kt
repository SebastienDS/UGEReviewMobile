package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.review.service.SessionManager

@Composable
fun Footer(navController: NavHostController, sessionManager: SessionManager, modifier: Modifier) {
    Row(modifier.background(Color.Gray)){
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f).fillMaxSize()){
            Icon(Icons.Default.Home, Modifier
                .fillMaxHeight()
                .padding(4.dp)) {
                navController.navigate("Home")
            }
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f).fillMaxSize()){
            Icon(Icons.Default.Search, Modifier
                .fillMaxHeight()
                .padding(4.dp)) {
                navController.navigate("Search")
            }
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f).fillMaxSize()){
            //TODO: METTRE UNE VRAI IMAGE ?
            Icon(Icons.Default.AccountCircle, Modifier
                .fillMaxHeight()
                .padding(4.dp)) {
                if (sessionManager.isAuthenticated()) {
                    navController.navigate("Profile/${sessionManager.getUserId()}")
                } else {
                    navController.navigate("Connection")
                }
            }
        }
    }
}