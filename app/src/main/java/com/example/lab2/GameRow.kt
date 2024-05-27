//package com.example.lab2
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun GameRow(
//    attemptColors: List<Color>,
//    isClickable: Boolean,
//    onSelectColor: (Int) -> Unit,
//    onCheck: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        attemptColors.forEachIndexed { index, color ->
//            Box(
//                modifier = Modifier
//                    .size(50.dp)
//                    .clip(CircleShape)
//                    .background(color)
//                    .clickable(enabled = isClickable) { onSelectColor(index) }
//            )
//        }
//        if (isClickable) {
//            Button(onClick = onCheck) {
//                Text("Check")
//            }
//        }
//    }
//}
//
//@Composable
//fun ColorPickerRow(colors: List<Color>, onColorSelected: (Color) -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceEvenly
//    ) {
//        colors.forEach { color ->
//            Box(
//                modifier = Modifier
//                    .size(50.dp)
//                    .clip(CircleShape)
//                    .background(color)
//                    .clickable { onColorSelected(color) }
//            )
//        }
//    }
//}
