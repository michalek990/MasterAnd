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
import android.util.Log
import com.example.lab2.db.DatabaseInstance
import com.example.lab2.db.Result
import kotlinx.coroutines.launch

@Composable
fun GameScreen(navController: NavController, colorCount: Int, userEmail: String) {
    val baseColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan)
    val secretCombo = remember { mutableStateOf(generateSecretCombo(baseColors, colorCount)) }
    val currentAttempt = remember { mutableStateOf(List(colorCount) { Color.Gray }) }
    val attempts = remember { mutableStateListOf<List<Color>>() }
    val indicators = remember { mutableStateListOf<List<Color>>() }
    val gameWon = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf<Color?>(null) }
    val coroutineScope = rememberCoroutineScope()

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
                    val indicator = generateAttemptIndicators(currentAttempt.value, secretCombo.value)
                    Log.d("GameScreen", "Attempt indicator: $indicator")
                    attempts.add(currentAttempt.value.toList())
                    indicators.add(indicator)
                    if (checkWinCondition(currentAttempt.value, secretCombo.value)) {
                        gameWon.value = true
                        coroutineScope.launch {
                            val bestResult = DatabaseInstance.database.userDao().getBestResultByEmail(userEmail)
                            if (bestResult == null || attempts.size < bestResult.score) {
                                DatabaseInstance.database.userDao().insertResult(
                                    Result(email = userEmail, score = attempts.size, colorCount = colorCount)
                                )
                            }
                            navController.navigate("resultScreen/${attempts.size}/$colorCount?userEmail=$userEmail")
                        }
                    }
                    currentAttempt.value = List(colorCount) { Color.Gray }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Display previous attempts with indicators
                LazyColumn {
                    items(attempts.size) { index ->
                        GameRowWithIndicator(
                            attemptColors = attempts[index],
                            indicators = indicators[index],
                            isClickable = false,
                            onSelectColor = {}
                        )
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
fun GameRowWithIndicator(
    attemptColors: List<Color>,
    indicators: List<Color>,
    isClickable: Boolean,
    onSelectColor: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            attemptColors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable(enabled = isClickable) { onSelectColor(index) }
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            indicators.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun GameRow(
    attemptColors: List<Color>,
    isClickable: Boolean,
    onSelectColor: (Int) -> Unit,
    onCheck: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        attemptColors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable(enabled = isClickable) { onSelectColor(index) }
            )
        }
        if (isClickable) {
            Button(onClick = onCheck) {
                Text("Check")
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

fun generateAttemptIndicators(attempt: List<Color>, secretCombo: List<Color>): List<Color> {
    val indicators = MutableList(attempt.size) { Color.Red }

    val attemptCopy = attempt.toMutableList()
    val secretCopy = secretCombo.toMutableList()

    // First pass: check for correct color and position
    for (i in attempt.indices) {
        if (attempt[i] == secretCombo[i]) {
            indicators[i] = Color.Black
            attemptCopy[i] = Color.Transparent
            secretCopy[i] = Color.Transparent
        }
    }

    // Second pass: check for correct color in wrong position
    for (i in attempt.indices) {
        if (attemptCopy[i] != Color.Transparent && attemptCopy[i] in secretCopy) {
            indicators[i] = Color.Yellow
            secretCopy[secretCopy.indexOf(attemptCopy[i])] = Color.Transparent
        }
    }

    return indicators
}
