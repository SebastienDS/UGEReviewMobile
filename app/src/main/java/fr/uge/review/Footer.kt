package fr.uge.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Footer(modifier: Modifier) {
    Row(modifier.background(Color.Gray)){
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f).fillMaxSize().clickable { /*TODO: NAVIGATION*/ }){
           //TODO: FAIRE DES DESSINS
           Text("HOME")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f).fillMaxSize().clickable { /*TODO: NAVIGATION*/ }){
            //TODO: FAIRE DES DESSINS
            Text("SEARCH")
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f).fillMaxSize().clickable { /*TODO: NAVIGATION*/ }){
            //TODO: FAIRE DES DESSINS
            Text("PROFILE")
        }
    }
}