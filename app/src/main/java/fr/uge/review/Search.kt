package fr.uge.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.review.dto.review.ReviewsDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme


@Composable
fun Search(navController: NavHostController, sessionManager: SessionManager, apiClient: ApiClient){
    var reviews: List<ReviewsDTO>? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }
    var search by remember { mutableStateOf("") }
    LaunchedEffect(search, page) {
        handleCall(apiClient.reviewService.searchReviews(search, page, 20)) {
            reviews = it
        }
    }
    Column {
        SearchComponent(
            Modifier
                .height(70.dp)
                .fillMaxWidth(),
            search) {
            search = it
        }
        Content(
            navController = navController, modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                reviews,
            {
                page--
            }
        ) {
            page++
        }
        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun SearchComponent(modifier: Modifier, search: String, onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf(search) }
    Row(
        modifier
            .padding(10.dp)
            .border(1.dp, Color.Black)){
        Icon(Icons.Default.Search, Modifier
            .fillMaxHeight()
            .padding(4.dp)) {
            onSearch(text)
        }

        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle.Default.copy(fontSize = 30.sp),
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp)
                .background(Color.Transparent))
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    ReviewTheme {
    }
}
