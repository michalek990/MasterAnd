import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun GameScreen(navController: NavController, colorCount: Int) {
    val availableColors = generateColors(colorCount)
    val secretCombo = remember { mutableStateOf(generateSecretCombo(availableColors, colorCount)) }
    val currentAttempt = remember { mutableStateOf(List(colorCount) { Color.Gray }) }
    val attempts = remember { mutableStateListOf<List<Color>>() }
    val gameWon = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        GameRow(
            attemptColors = currentAttempt.value,
            isClickable = !gameWon.value,
            onSelectColor = { colorIndex ->
                currentAttempt.value = currentAttempt.value.toMutableList().apply {
                    set(colorIndex, getNextColor(currentAttempt.value[colorIndex], availableColors))
                }
            },
            onCheck = {
                attempts.add(currentAttempt.value.toList())
                currentAttempt.value = List(colorCount) { Color.Gray }
                if (checkWinCondition(attempts.last(), secretCombo.value)) {
                    gameWon.value = true
                }
            },
            secretCombo = secretCombo.value // Przekazujemy secretCombo jako argument
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Display previous attempts
        LazyColumn {
            items(attempts) { attempt ->
                GameRow(
                    attemptColors = attempt,
                    isClickable = false,
                    onSelectColor = {},
                    onCheck = {},
                    secretCombo = secretCombo.value // Przekazujemy secretCombo jako argument
                )
            }
        }

        if (gameWon.value) {
            LaunchedEffect(gameWon.value) {
                navController.navigate("resultScreen/${attempts.size}")
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
    }
}
fun generateSecretCombo(availableColors: List<Color>, comboSize: Int): List<Color> {
    return availableColors.shuffled().take(comboSize)
}

fun getNextColor(currentColor: Color, availableColors: List<Color>): Color {
    val currentIndex = availableColors.indexOf(currentColor)
    return availableColors[(currentIndex + 1) % availableColors.size]
}

fun checkWinCondition(attempt: List<Color>, secretCombo: List<Color>): Boolean {
    return attempt == secretCombo
}

fun generateColors(count: Int): List<Color> {
    // Generuj dynamicznie listę kolorów na podstawie podanej liczby
    val baseColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan)
    return baseColors.shuffled().take(count)
}

@Composable
fun GameRow(
    attemptColors: List<Color>,
    isClickable: Boolean,
    onSelectColor: (Int) -> Unit,
    onCheck: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        attemptColors.forEachIndexed { index, color ->
            val feedback = if (!isClickable) getFeedbackForColor(attemptColors, secretCombo.value, index) else Feedback.None
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable(enabled = isClickable) { onSelectColor(index) }
            ) {
                if (!isClickable) {
                    when (feedback) {
                        Feedback.Correct -> {
                            Icon(Icons.Default.Check, contentDescription = "Correct color", tint = Color.White)
                        }
                        Feedback.CorrectAndPlaced -> {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color.Yellow)
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
        if (isClickable) {
            Button(onClick = onCheck) {
                Text("Check")
            }
        }
    }
}

enum class Feedback {
    None,
    Correct,
    CorrectAndPlaced
}

fun getFeedbackForColor(attemptColors: List<Color>, secretCombo: List<Color>, index: Int): Feedback {
    val color = attemptColors[index]
    val correctColor = secretCombo.contains(color)
    val correctPosition = secretCombo[index] == color

    return when {
        correctColor && correctPosition -> Feedback.CorrectAndPlaced
        correctColor -> Feedback.Correct
        else -> Feedback.None
    }
}