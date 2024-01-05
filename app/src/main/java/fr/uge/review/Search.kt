package fr.uge.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.review.ui.theme.ReviewTheme

@Composable
fun Search(){
    Column {
        SearchComponent(
            Modifier
                .height(70.dp)
                .fillMaxWidth())
        Content(modifier = Modifier
            .weight(1f)
            .fillMaxWidth())
        Footer(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

@Composable
fun SearchComponent(modifier: Modifier) {
    var text by remember { mutableStateOf("") }
    Row(modifier.padding(10.dp).border(1.dp, Color.Black)){
        Icon(Icons.Default.Search, Modifier
            .fillMaxHeight()
            .padding(4.dp)) {
            // Handle search icon click
            //keyboardController?.hide()
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
            modifier = modifier.fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp)
                .background(Color.Transparent))
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    ReviewTheme {
        Search()
    }
}
