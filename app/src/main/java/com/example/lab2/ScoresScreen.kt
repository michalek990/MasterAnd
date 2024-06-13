package com.example.lab2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab2.db.DatabaseInstance
import com.example.lab2.db.Result
import kotlinx.coroutines.launch

@Composable
fun ScoresScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var results by remember { mutableStateOf(emptyList<Result>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            results = DatabaseInstance.database.userDao().getAllResults()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Results", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(results) { result ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Email: ${result.email}", style = MaterialTheme.typography.bodyLarge)
                    Text("Score: ${result.score}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    DatabaseInstance.database.userDao().deleteAllResults()
                    results = emptyList()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear All Results")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("startScreen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}
