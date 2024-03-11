package fr.uge.review

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.uge.review.dto.ReviewDTO
import fr.uge.review.service.ApiService
import fr.uge.review.ui.theme.ReviewTheme
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReviewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Application()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ApiService::class.java)

    val call = service.fetchData()
    Log.i("Ara Ara", "UwU")
    call.enqueue(object : Callback<List<ReviewDTO>> {
        override fun onResponse(call: Call<List<ReviewDTO>>, response: Response<List<ReviewDTO>>) {
            if (response.isSuccessful) {
                val data = response.body()
                Log.i("UwU", data.toString())
            } else {
                Log.i("OwO", "OwO")
            }
        }

        override fun onFailure(call: Call<List<ReviewDTO>>, t: Throwable) {
            Log.e("Rawr", "Call failed: ${t.message}", t)
        }
    })

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable("Home") {
            Home(navController = navController)
        }
        composable("Search") {
            Search(navController = navController)
        }
        composable("Friends") {
            Friends(navController = navController)
        }
        composable("Connection") {
            Connection(navController = navController)
        }
        composable("Review") {
            Review(navController = navController)
        }
        composable("Profile") {
            Profile(navController = navController)
        }
    }
}

@Composable
fun Application() {
    AppNavigation()
}