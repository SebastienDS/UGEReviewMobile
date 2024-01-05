package fr.uge.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import fr.uge.review.ui.theme.ReviewTheme

@Composable
fun Menu(navController: NavHostController, modifier: Modifier) {
    Column(modifier.background(Color.White)){
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clickable { /*TODO: NAVIGATION when review page done*/ }){
            //TODO: AFFICHER LES REVIEWS
            Text("REVIEWS")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clickable { /*TODO: NAVIGATION when response page done*/ }){
            //TODO: AFFICHER LES REPONSES
            Text("YOUR RESPONSES")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clickable { navController.navigate("Friends") }){
            Text("YOUR FRIENDS")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clickable { /*TODO: NAVIGATION when page Like is done*/ }){
            //TODO: AFFICHER LES LIKES
            Text("YOUR LIKES")
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
fun Profile(navController: NavHostController) {
    Column {
        Menu(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth())
        Footer(navController, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectedProfilePreview() {
    ReviewTheme {
        Profile(rememberNavController())
    }
}