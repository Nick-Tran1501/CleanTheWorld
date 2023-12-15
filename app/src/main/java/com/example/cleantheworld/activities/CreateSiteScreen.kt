package com.example.cleantheworld.activities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.models.DirtyLevel
import com.example.cleantheworld.ui.components.LocationPicker
import com.example.cleantheworld.ui.components.SortDropdown
import com.google.android.gms.maps.model.LatLng

@Composable
fun CreateSiteScreen() {
    var siteLocation by remember { mutableStateOf<LatLng?>(null) }


    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf(DirtyLevel.CLEANED) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var participantIds by remember { mutableStateOf(listOf<String>()) }
    var adminId by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("None") }

    Column {
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
                color = MaterialTheme.colorScheme.onBackground
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
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        // Dropdown for selecting DirtyLevel
//        DropdownMenuForDirtyLevel(selectedLevel) { selectedLevel = it }
        SortDropdown(onSortChanged = { option ->
            sortOption = option
        })


        LocationPicker(onLocationSelected = { latLng ->
            siteLocation = latLng
        })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newSite = CleanUpSite(
                    name = name,
                    description = description,
                    level = selectedLevel,
                    latitude = selectedLocation?.latitude ?: 0.0,
                    longitude = selectedLocation?.longitude ?: 0.0,
                    participantIds = participantIds,
                    adminId = adminId
                )
//                onSiteCreated(newSite)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Site")
        }
    }
}

@Composable
fun DropdownMenuForDirtyLevel(selectedLevel: DirtyLevel, onLevelSelected: (DirtyLevel) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary))
    ) {
        Text(selectedLevel.name, Modifier.clickable { expanded = true })
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DirtyLevel.values().forEach { level ->
                DropdownMenuItem(text = { level.name }, onClick = {
                    onLevelSelected(level)
                    expanded = false
                })
            }
        }
    }
}