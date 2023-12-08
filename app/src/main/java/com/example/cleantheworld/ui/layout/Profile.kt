package com.example.cleantheworld.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.authentication.UserAuthManager
import com.example.cleantheworld.models.User
import com.example.cleantheworld.utils.ThemeViewModel

@Composable
fun MyProfileScreen(user: User, themeViewModel: ThemeViewModel, navController: NavController) {
    var editingName by remember { mutableStateOf(false) }
    var editingAge by remember { mutableStateOf(false) }
    var editingPhone by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(user.name) }
    var age by remember { mutableStateOf(user.age.toString()) }
    var phone by remember { mutableStateOf(user.phone) }
    val isDarkTheme = themeViewModel.isDarkTheme.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Profile",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Name field
        ProfileField("Name", name, editingName, onEdit = { editingName = true }) {
            name = it
            editingName = false
            // Update user's name in database or ViewModel
        }

        // Age field
        ProfileField("Age", age, editingAge, onEdit = { editingAge = true }) {
            age = it
            editingAge = false
            // Update user's age in database or ViewModel
        }

        // Phone field
        ProfileField("Phone", phone, editingPhone, onEdit = { editingPhone = true }) {
            phone = it
            editingPhone = false
            // Update user's phone in database or ViewModel
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edit buttons
        Button(onClick = { /* Handle edit name */ }) {
            Text("Edit Name")
        }
        Button(onClick = { /* Handle edit age */ }) {
            Text("Edit Age")
        }
        Button(onClick = { /* Handle edit phone */ }) {
            Text("Edit Phone")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dark mode toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { themeViewModel.toggleTheme() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log out button
        Button(onClick = {
            UserAuthManager.logOutUser {
                // Navigate to the login screen or perform other actions post-logout
                navController.navigate("login_screen") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }) {
            Text("Log Out")
        }
    }
}


@Composable
fun ProfileField(
    label: String,
    value: String,
    editing: Boolean,
    onEdit: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (editing) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(label) }
            )
            Button(onClick = { onSubmit(text) }) {
                Text("Submit")
            }
        } else {
            Text("$label: $value")
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
        }
    }
}