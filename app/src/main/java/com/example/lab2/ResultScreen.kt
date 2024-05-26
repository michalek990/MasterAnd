package com.example.lab2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ResultScreen(attempts: Int, navController: NavController, colorCount: Int) {
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
                navController.popBackStack()
                navController.navigate("gameScreen/$colorCount")
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
