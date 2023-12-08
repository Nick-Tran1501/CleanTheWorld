package com.example.cleantheworld.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.R
import com.example.cleantheworld.authentication.UserAuthManager
import com.example.cleantheworld.ui.components.showToast
import com.example.cleantheworld.utils.ThemeViewModel


@Composable
fun RegisterScreenActivity(navController: NavController, themeViewModel: ThemeViewModel) {
    val isDarkTheme = themeViewModel.isDarkTheme.value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val textToastColorSuccess = MaterialTheme.colorScheme.onPrimary.toArgb()
    val successColor = MaterialTheme.colorScheme.primary.toArgb()
    val textToastColorError = MaterialTheme.colorScheme.onError.toArgb()
    val errorColor = MaterialTheme.colorScheme.error.toArgb()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.navigate("login_screen")}) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back),
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { themeViewModel.toggleTheme() }) {
                Icon(
                    imageVector = if (isDarkTheme) ImageVector.vectorResource(id = R.drawable.light_mode) else ImageVector.vectorResource(
                        id = R.drawable.dark_mode
                    ),
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Image(
            painter = if (!isDarkTheme) painterResource(id = R.drawable.light_theme) else painterResource(
                id = R.drawable.dark_theme
            ),
            contentDescription = "Application Icon Theme",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Text(
            text = "Register Form",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    if (passwordVisible)
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.visibility),
                            contentDescription = "Show Password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    else
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.visibilityoff),
                            contentDescription = "Show Password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank() || age.isBlank() || phone.isBlank()) {
                    showToast(context, "Please fill in all fields", errorColor, textToastColorError)
                } else {
                    UserAuthManager.registerUser(email, password, name, age, phone, onSuccess = {
                        // Handle successful login
                        showToast(
                            context,
                            "Register Successfully",
                            successColor,
                            textToastColorSuccess
                        )
                        navController.navigate("home_screen")
                    }, onError = { error ->
                        errorMessage = error
                        showToast(context, errorMessage.toString(), errorColor, textToastColorError)
                    })
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}
