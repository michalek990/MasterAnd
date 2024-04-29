package com.example.lab2

import GameScreen
import StartScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        composable("gameScreen/{colorCount}") { backStackEntry ->
            GameScreen(navController, backStackEntry.arguments?.getString("colorCount")?.toInt() ?: 4)
        }
        composable("resultScreen/{attempts}") { backStackEntry ->
            val attempts = backStackEntry.arguments?.getString("attempts")?.toInt() ?: 0
            ResultScreen(attempts, navController)
        }
    }
}