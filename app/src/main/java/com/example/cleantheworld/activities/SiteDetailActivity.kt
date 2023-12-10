package com.example.cleantheworld.activities

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.cleantheworld.models.CleanUpSite

@Composable
fun SiteDetailActivity(site: CleanUpSite, navController: NavController) {
    Text(site.name)
}