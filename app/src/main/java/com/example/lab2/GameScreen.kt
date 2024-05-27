package com.example.lab2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GameScreen(navController: NavController, colorCount: Int) {
    val baseColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan)
    val secretCombo = remember { mutableStateOf(generateSecretCombo(baseColors, colorCount)) }
    val currentAttempt = remember { mutableStateOf(List(colorCount) { Color.Gray }) }
    val attempts = remember { mutableStateListOf<List<Color>>() }
    val gameWon = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf<Color?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GameRow(
                    attemptColors = currentAttempt.value,
                    isClickable = !gameWon.value,
                    onSelectColor = { colorIndex ->
                        selectedColor.value?.let { color ->
                            currentAttempt.value = currentAttempt.value.toMutableList().apply {
                                set(colorIndex, color)
                            }
                        }
                    },
                ) {
                    attempts.add(currentAttempt.value.toList())
                    currentAttempt.value = List(colorCount) { Color.Gray }
                    if (checkWinCondition(attempts.last(), secretCombo.value)) {
                        gameWon.value = true
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Display previous attempts
                LazyColumn {
                    items(attempts) { attempt ->
                        GameRow(
                            attemptColors = attempt,
                            isClickable = false,
                            onSelectColor = {},
                        ) {}
                    }
                }

                if (gameWon.value) {
                    LaunchedEffect(gameWon.value) {
                        navController.navigate("resultScreen/${attempts.size}/$colorCount")
                    }
                }

                // Display the secret combo at the bottom of the screen
                Row(modifier = Modifier.padding(top = 20.dp)) {
                    Text("Secret Combo:", style = MaterialTheme.typography.titleMedium)
                    secretCombo.value.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(horizontal = 4.dp)
                                .background(color)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Add an empty spacer to push content to the top
            Spacer(modifier = Modifier.weight(1f))

            // Column for the back button and color picker row at the bottom
            Column {
                Button(
                    onClick = { navController.navigate("startScreen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Back to Home")
                }

                // Color Picker Row at the bottom of the screen
                ColorPickerRow(baseColors) { color ->
                    selectedColor.value = color
                }
            }
        }
    }
}

@Composable
fun ColorPickerRow(colors: List<Color>, onColorSelected: (Color) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

fun generateSecretCombo(availableColors: List<Color>, comboSize: Int): List<Color> {
    return availableColors.shuffled().take(comboSize)
}

fun checkWinCondition(attempt: List<Color>, secretCombo: List<Color>): Boolean {
    return attempt == secretCombo
}
