package com.example.lab2

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab2.Game.GameScreen
import com.example.lab2.ui.theme.Lab2Theme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberAnimatedNavController()
    Lab2Theme {
        AnimatedNavHost(navController = navController, startDestination = "startScreen") {
            composable(
                "startScreen",
                enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700)) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700)) + fadeOut() }
            ) { StartScreen(navController) }
            composable(
                "gameScreen/{colorCount}?userEmail={userEmail}",
                arguments = listOf(
                    navArgument("colorCount") { type = NavType.IntType },
                    navArgument("userEmail") { type = NavType.StringType }
                ),
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) + fadeOut() }
            ) { backStackEntry ->
                val colorCount = backStackEntry.arguments?.getInt("colorCount") ?: 4
                val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
                GameScreen(navController, colorCount, userEmail)
            }
            composable(
                "resultScreen/{attempts}/{colorCount}?userEmail={userEmail}",
                arguments = listOf(
                    navArgument("attempts") { type = NavType.IntType },
                    navArgument("colorCount") { type = NavType.IntType },
                    navArgument("userEmail") { type = NavType.StringType }
                ),
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) + fadeOut() }
            ) { backStackEntry ->
                val attempts = backStackEntry.arguments?.getInt("attempts") ?: 0
                val colorCount = backStackEntry.arguments?.getInt("colorCount") ?: 4
                val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
                ResultScreen(attempts, navController, colorCount, userEmail)
            }
            composable(
                "resultsScreen",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) + fadeOut() }
            ) {
                ScoresScreen(navController)
            }
        }
    }
}
