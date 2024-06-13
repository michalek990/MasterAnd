package com.example.lab2

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lab2.db.DatabaseInstance
import com.example.lab2.db.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var colorCount by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isEmailValid by remember { mutableStateOf(true) }
    var isNameValid by remember { mutableStateOf(true) }
    var isColorCountValid by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )

    val scale = rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome to MasterAnd",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.scale(scale.value)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            selectedImageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Text("Select Avatar", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            isError = !isNameValid,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isNameValid) {
            Text("Name must be at least 4 characters", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = !isEmailValid,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isEmailValid) {
            Text("Invalid email address", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = colorCount,
            onValueChange = { colorCount = it },
            label = { Text("Enter number of colors (3-6)") },
            singleLine = true,
            isError = !isColorCountValid,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isColorCountValid) {
            Text("Number of colors must be between 3 and 6", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            val count = colorCount.toIntOrNull()
            isEmailValid = email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))
            isNameValid = name.length >= 4
            isColorCountValid = count != null && count in 3..6

            if (isEmailValid && isNameValid && isColorCountValid) {
                coroutineScope.launch {
                    val existingUser = DatabaseInstance.database.userDao().getUserByEmail(email)
                    if (existingUser != null) {
                        if (existingUser.name != name) {
                            DatabaseInstance.database.userDao().insertUser(
                                User(email = email, name = name, avatarUri = selectedImageUri?.toString())
                            )
                        }
                    } else {
                        DatabaseInstance.database.userDao().insertUser(
                            User(email = email, name = name, avatarUri = selectedImageUri?.toString())
                        )
                    }
                    navController.navigate("gameScreen/$count?userEmail=$email")
                }
            }
        }) {
            Text("Start Game")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("resultsScreen")
        }) {
            Text("High Scores")
        }
    }
}
