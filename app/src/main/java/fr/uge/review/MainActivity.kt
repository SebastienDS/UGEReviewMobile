package fr.uge.review

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.uge.review.service.SessionManager
import fr.uge.review.ui.theme.ReviewTheme

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
                    val sessionManager = SessionManager(this)
                    val apiClient = ApiClient(this)

                    Application(apiClient, sessionManager)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(apiClient: ApiClient, sessionManager: SessionManager) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable("Home") {
            Home(navController = navController, sessionManager, apiClient)
        }
        composable("Search") {
            Search(navController = navController, sessionManager, apiClient)
        }
        composable(route = "Users/{userId}/reviews",
                arguments = listOf(
                    navArgument("userId") {
                        type = NavType.LongType
                    }
                )) {
            val userId = it.arguments!!.getLong("userId")
            UserReviews(navController = navController, userId, sessionManager, apiClient)
        }
        composable("Friends") {
            Friends(navController = navController, sessionManager)
        }
        composable("Connection") {
            Connection(navController = navController, apiClient = apiClient, sessionManager = sessionManager)
        }
        composable(
            route = "Review/{reviewId}",
            arguments = listOf(
                navArgument("reviewId") {
                    type = NavType.LongType
                }
            )
        ) {
            val reviewId = it.arguments!!.getLong("reviewId")
            Review(navController = navController, reviewId = reviewId, apiClient = apiClient, sessionManager = sessionManager)
        }
        composable(
            route = "Profile/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.LongType
                }
            )
        ) {
            val userId = it.arguments!!.getLong("userId")
            Profile(navController = navController, userId = userId, apiClient = apiClient, sessionManager = sessionManager)
        }
        composable("Signup") {
            Signup(navController = navController, apiClient = apiClient, sessionManager = sessionManager)
        }
        composable("CreateReview") {
            CreateReview(navController = navController, apiClient = apiClient, sessionManager = sessionManager)
        }
        composable("Notifications") {
            Notifications(navController = navController, apiClient = apiClient, sessionManager = sessionManager)
        }
    }
}

@Composable
fun Application(apiClient: ApiClient, sessionManager: SessionManager) {
    AppNavigation(apiClient, sessionManager)
}