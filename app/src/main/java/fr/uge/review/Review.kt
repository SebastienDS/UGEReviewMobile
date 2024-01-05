package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.review.ui.theme.ReviewTheme
import java.text.SimpleDateFormat
import java.util.Date

data class Response(val content: String)
data class MetaData(val title: String, val user: String, val date: Date, val description: String)
data class Review(val metadata: MetaData, val content: String, val responses: List<Response>)

@Composable
fun Review(navController: NavHostController) {
    val review = Review(
        MetaData("Title", "User", Date(), "BLABLABLABLA"),
        "zegolzbheeguozhegzheozhgaopehgaopehgaopihegopiaehgoiahengahnegoiahnegoabhegoabegoaubegag",
        listOf(Response("egfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeegfihzegiophzeghizeg"), Response("zergzegzeg"))
    )

    Column {
        ReviewViewer(navController, review, modifier = Modifier
            .weight(1f)
            .background(Color.White)
            .fillMaxWidth())
        Footer(navController, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun ReviewViewer(navController: NavHostController, review: Review, modifier: Modifier) {
    LazyColumn(modifier) {
        item {
            ReviewHeader(navController, review.metadata)
            ReviewContent(review, modifier = Modifier.padding(20.dp, 10.dp))

            Text("31 Réponses:", Modifier.padding(3.dp))

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
        items(review.responses) {
            ResponseItem(it)
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
fun ReviewHeader(navController: NavHostController, metadata: MetaData) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
           Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.Center) {
               Text(metadata.title, fontSize = 30.sp)
           }
           Column(Modifier.weight(1f)) {
               Text(metadata.user, Modifier.clickable { navController.navigate("Profile") })
               Text(SimpleDateFormat("dd/MM/yyyy").format(metadata.date))
           }
        }

        Text(metadata.description, modifier = Modifier.padding(20.dp, 10.dp))
    }
}

@Composable
fun ReviewContent(review: Review, modifier: Modifier) {
    Text(review.content, modifier = modifier
        .border(1.dp, Color.Black)
        .padding(20.dp))
}

@Composable
fun ResponseItem(response: Response) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.KeyboardArrowUp, Modifier
                .fillMaxHeight()) {
                Log.i("test", "Poce blo")
            }
            Text("546")
            Icon(Icons.Default.KeyboardArrowDown, Modifier
                .fillMaxHeight()) {
                Log.i("test", "Pas Poce blo")
            }
        }
        Box(Modifier.weight(1f)) {
            Text(text = response.content, color = Color.Black, modifier = Modifier.padding(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewPreview() {
    ReviewTheme {
        Review(rememberNavController())
    }
}