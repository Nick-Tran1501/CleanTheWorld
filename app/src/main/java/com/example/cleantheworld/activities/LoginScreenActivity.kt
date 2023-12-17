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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.R
import com.example.cleantheworld.authentication.UserAuthManager
import com.example.cleantheworld.ui.components.showToast
import com.example.cleantheworld.utils.ThemeViewModel

@Composable
fun LoginScreenActivity(navController: NavController, themeViewModel: ThemeViewModel) {
    val isDarkTheme = themeViewModel.isDarkTheme.value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
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
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(onClick = { themeViewModel.toggleTheme() }) {
                Icon(
                    imageVector = if (isDarkTheme) ImageVector.vectorResource(id = R.drawable.light_mode) else ImageVector.vectorResource(
                        id = R.drawable.dark_mode
                    ),
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = if (!isDarkTheme) painterResource(id = R.drawable.light_theme) else painterResource(
                    id = R.drawable.dark_theme
                ),
                contentDescription = "Application Icon Theme",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
//            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        if (passwordVisible)
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.visibility),
                                contentDescription = "Show Password",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        else
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.visibilityoff),
                                contentDescription = "Show Password",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                    }
                },
//            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(onDone = {
//                })
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        showToast(
                            context,
                            "Please fill in all fields",
                            errorColor,
                            textToastColorError
                        )
                    } else {
                        UserAuthManager.loginUser(email, password, onSuccess = {
                            // Handle successful login
                            navController.navigate("home_screen")
                        }, onError = { error ->
                            errorMessage = error
                        })
                    }

                },
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text("Login")
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text("Don't have an account?", color = MaterialTheme.colorScheme.onBackground)
                TextButton(onClick = {
                    // Navigate to registration screen
                    navController.navigate("register_screen")
                }) {
                    Text("Register")
                }
            }
        }



    }
}