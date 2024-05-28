package com.example.lab2

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FeedbackCircles(
    attemptColors: List<Color>,
    indicators: List<Color>,
    isClickable: Boolean,
    onSelectColor: (Int) -> Unit
) {
    val transition = rememberInfiniteTransition(label = "")
    val animatedColors = indicators.mapIndexed { index, color ->
        transition.animateColor(
            initialValue = Color.Red,
            targetValue = color,
            animationSpec = infiniteRepeatable(
                animation = TweenSpec<Color>(durationMillis = 3000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
    }

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
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val gridSize = 2
            val rows = animatedColors.chunked(gridSize)

            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { animatedColor ->
                        val color by animatedColor
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .then(
                                    if (color == Color.Transparent) {
                                        Modifier.border(2.dp, Color.Gray, CircleShape)
                                    } else {
                                        Modifier.background(color)
                                    }
                                )
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
