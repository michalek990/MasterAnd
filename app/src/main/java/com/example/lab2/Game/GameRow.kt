package com.example.lab2.Game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GameRow(
    attemptColors: List<Color>,
    isClickable: Boolean,
    onSelectColor: (Int) -> Unit,
    onCheck: () -> Unit
) {
    val allColorsSelected = attemptColors.all { it != Color.Gray }
    val animatedScale by animateFloatAsState(
        targetValue = if (allColorsSelected) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

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
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .scale(animatedScale)
            ) {
                if (allColorsSelected) {
                    Button(onClick = onCheck) {
                        Text("✓")
                    }
                }
            }
        }
    }
}
