package fr.uge.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.review.ui.theme.ReviewTheme

@Composable
fun Connection(navController: NavHostController){
    var username by remember{ mutableStateOf("")}
    var password by remember{ mutableStateOf("")}
    Box(){

    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                value = username,
                onValueChange = { username = it },
                textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                modifier = Modifier
                    .size(300.dp, 60.dp)
                    .padding(start = 16.dp, end = 8.dp)
                    .border(1.dp, Color.Black)
                    .background(Color.Transparent)
            )
            Column (Modifier.padding(bottom = 20.dp, top = 20.dp)) {
                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                    modifier = Modifier
                        .size(300.dp, 60.dp)
                        .padding(start = 16.dp, end = 8.dp)
                        .border(1.dp, Color.Black)
                        .background(Color.Transparent)
                )
                Text("Mot de passe oublié", color = Color.Gray, modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { /*TODO: navigate when page done*/})
            }
            Text("Connect", modifier = Modifier
                .padding(start = 180.dp)
                .border(1.dp, Color.Black)
                .padding(20.dp, 15.dp)
                .clickable { //TODO verif connection
                    navController.navigate("Profile")
                })
        }
        Footer(navController,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )
    }

}


@Preview(showBackground = true)
@Composable
fun ConnectionPreview() {
    ReviewTheme {
        Connection(rememberNavController())
    }
}
