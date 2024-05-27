package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab2.db.DatabaseInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseInstance.init(this)
        setContent {
            AppNavigation()
        }
    }
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "startScreen") {
        composable("startScreen") { StartScreen(navController) }
        composable("gameScreen/{colorCount}?userEmail={userEmail}") { backStackEntry ->
            val colorCount = backStackEntry.arguments?.getString("colorCount")?.toInt() ?: 4
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: "user@example.com"
            GameScreen(navController, colorCount, userEmail)
        }
        composable("resultScreen/{attempts}/{colorCount}?userEmail={userEmail}") { backStackEntry ->
            val attempts = backStackEntry.arguments?.getString("attempts")?.toInt() ?: 0
            val colorCount = backStackEntry.arguments?.getString("colorCount")?.toInt() ?: 4
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: "user@example.com"
            ResultScreen(attempts, navController, colorCount, userEmail)
        }
        composable("resultsScreen") {
            ScoresScreen(navController)
        }
    }
}

