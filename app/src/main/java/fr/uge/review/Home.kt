package fr.uge.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.review.ui.theme.ReviewTheme

@Composable
fun Home(navController: NavHostController) {
    Column {
        Content(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth())
        Footer(navController, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

data class test(val string: String)

@Composable
fun Content(navController: NavHostController, modifier: Modifier, ){
    var list = listOf(test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"),test("cc"), test("cc"), test("cc"),test("cc") )
    LazyColumn(modifier = modifier){
        items(list){
            Text(it.string, modifier.clickable { navController.navigate("Review/4") })
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    ReviewTheme {
        Home(rememberNavController())
    }
}