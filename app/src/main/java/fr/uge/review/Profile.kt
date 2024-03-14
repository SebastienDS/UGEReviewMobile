package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import fr.uge.review.dto.user.UserDataDTO
import fr.uge.review.dto.user.UserLoginDTO
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
fun Profile(
    navController: NavHostController,
    userId: Long,
    apiClient: ApiClient,
    sessionManager: SessionManager
) {
    if (!sessionManager.isAuthenticated()) {
        navController.navigate("Connection")
    }
    Column {
        Menu(navController, modifier = Modifier
            .weight(1f)
            .fillMaxWidth())

        Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                    .clickable {
                        logout(apiClient = apiClient, sessionManager = sessionManager,
                            onSuccess = {
                                navController.navigate("Connection")
                            }, onFailure = {})
                    }){
                Text("Se déconnecter")
            }
            if (sessionManager.isAuthenticated() && sessionManager.getUserId() == userId) {
                Box(modifier = Modifier
                    .clickable {
                        deleteProfile(apiClient = apiClient, sessionManager = sessionManager,
                            onSuccess = {
                                navController.navigate("Connection")
                            }, onFailure = {})
                    }){
                    Text("Supprimer mon compte")
                }
            }
        }

        Footer(navController, sessionManager = sessionManager, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth())
    }
}

fun logout(apiClient: ApiClient, sessionManager: SessionManager,
             onSuccess: () -> Unit, onFailure: (Throwable?) -> Unit)  {
    apiClient.userService.logout()
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO logout", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU LOGOUT SUCCESS")
                    sessionManager.clear()
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO LOGOUT FAIL")
                    onFailure(null)
                }
            }
        })
}

fun deleteProfile(apiClient: ApiClient, sessionManager: SessionManager,
           onSuccess: () -> Unit, onFailure: (Throwable?) -> Unit)  {
    apiClient.userService.deleteProfile()
        .enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UwU",  "OwO deleteProfile", t)
                onFailure(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("UwU", "UwU deleteProfile SUCCESS")
                    sessionManager.clear()
                    onSuccess()
                } else {
                    Log.e("UwU", "OwO deleteProfile FAIL")
                    onFailure(null)
                }
            }
        })
}
