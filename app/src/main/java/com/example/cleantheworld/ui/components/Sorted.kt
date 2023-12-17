package com.example.cleantheworld.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.cleantheworld.R

@Composable
fun SortDropdown(onSortChanged: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp)
    ) {
        OutlinedTextField(
            value = selectedOption,
            label = {
                Text(
                    "Dirty Level",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },

            modifier = Modifier.clickable { expanded = true },
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.down),
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.clickable { expanded = !expanded },
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            readOnly = true,
            onValueChange = { selectedOption = it },

//            textStyle = MaterialTheme.typography.labelLarge,

//                .border(0.dp,MaterialTheme.colorScheme.onPrimaryContainer)


        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("High", "Medium", "Low", "Cleaned").forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOption = option
                    expanded = false
                    onSortChanged(selectedOption)
                }) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}