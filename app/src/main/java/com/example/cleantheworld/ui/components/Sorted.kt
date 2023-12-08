package com.example.cleantheworld.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortDropdown(onSortChanged: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Dirty Level") }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp)
    ) {
        OutlinedTextField(
            value = "Sort by: $selectedOption",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            textStyle = MaterialTheme.typography.caption,
            modifier = Modifier
                .clickable { expanded = true }
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
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}
