package com.example.lab2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab2.db.DatabaseInstance
import com.example.lab2.db.Result
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(attempts: Int, navController: NavController, colorCount: Int, userEmail: String) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Results",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Recent score: $attempts",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    val bestResult = DatabaseInstance.database.userDao().getBestResultByEmail(userEmail)
                    if (bestResult == null || attempts < bestResult.score) {
                        DatabaseInstance.database.userDao().insertResult(
                            Result(email = userEmail, score = attempts, colorCount = colorCount)
                        )
                    }
                    navController.popBackStack()
                    navController.navigate("gameScreen/$colorCount?userEmail=$userEmail")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Play Again")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("startScreen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
