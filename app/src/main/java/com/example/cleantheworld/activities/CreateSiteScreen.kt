package com.example.cleantheworld.activities

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.R
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.models.DirtyLevel
import com.example.cleantheworld.myFirebaseManager.CleanUpSiteManager
import com.example.cleantheworld.ui.components.LocationPicker
import com.example.cleantheworld.ui.components.SortDropdown
import com.example.cleantheworld.utils.TAG
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@Composable
fun CreateSiteScreen(navController: NavController, curUserId: String) {
    val scope = rememberCoroutineScope()
    var siteLocation by remember { mutableStateOf<LatLng?>(null) }
    var name by remember { mutableStateOf("") }
    var shortDescription by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf(DirtyLevel.CLEANED) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var participantIds by remember { mutableStateOf(listOf<String>()) }
    var adminId by remember { mutableStateOf(curUserId) }
    var sortOption by remember { mutableStateOf("None") }

    Column {
        Row {
            IconButton(onClick = { navController.navigate("my_list_of_sites") }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back),
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .padding()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Create Site Form",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = shortDescription,
            onValueChange = { shortDescription = it },
            label = { Text("Short Description") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        SortDropdown(onSortChanged = { option ->
            sortOption = option
        })


        LocationPicker(onLocationSelected = { latLng ->
            siteLocation = latLng
            selectedLocation = siteLocation
            Log.d(TAG, "onLocationSelected: $selectedLocation")
        })
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val newSite = CleanUpSite(
                        name = name,
                        description = description,
                        shortDescription = shortDescription,
                        level = selectedLevel,
                        latitude = selectedLocation?.latitude ?: 0.0,
                        longitude = selectedLocation?.longitude ?: 0.0,
                        participantIds = participantIds.plus(adminId),
                        adminId = adminId
                    )
                    scope.launch { CleanUpSiteManager.createSite(newSite) }
                    navController.navigate("my_list_of_sites")
                },
            ) {
                Text("Create Site")
            }

        }
    }
}