package com.example.cleantheworld.activities

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cleantheworld.authentication.UserAuthManager
import com.example.cleantheworld.authentication.UserAuthManager.getCurUser
import com.example.cleantheworld.models.User
import com.example.cleantheworld.ui.components.BottomNavigationBar
import com.example.cleantheworld.ui.layout.ListOfSites
import com.example.cleantheworld.ui.layout.MyProfileScreen
import com.example.cleantheworld.ui.layout.MySites
import com.example.cleantheworld.ui.layout.NearbySites
import com.example.cleantheworld.utils.ThemeViewModel

@Composable
fun HomeScreenActivity(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf(UserAuthManager.curUser) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(key1 = UserAuthManager.curUser) {
        currentUser = getCurUser()
    }

    Scaffold(
        bottomBar = {
            if (currentRoute !== "login_screen" && currentUser != null) {
                currentUser?.let { BottomNavigationBar(navController, it.admin) }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = determineStartDestination(currentUser),
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login_screen") {
                LoginScreenActivity(navController, themeViewModel)
            }
            composable("register_screen") {
                RegisterScreenActivity(navController, themeViewModel)
            }
            composable("home_screen") {
                HomeScreenActivity(themeViewModel)
            }
            composable("list_of_sites") {
                currentUser?.let { it1 ->
                    ListOfSites(
                        themeViewModel,
                        navController,
                        it1.id
                    )
                }
            }
            composable("nearby_list_of_sites") { NearbySites(themeViewModel) }
            composable("my_list_of_sites") {
                currentUser?.let { it1 ->
                    MySites(
                        it1.id,
                        themeViewModel,
                        navController,
                    )
                }
            }
            composable("site_detail/{siteId}") { backStackEntry ->
                SiteDetailActivity(
                    siteId = backStackEntry.arguments?.getString("siteId") ?: "",
                    navController
                )
            }
            composable("profile") {
                currentUser?.let { it1 ->
                    MyProfileScreen(
                        it1,
                        themeViewModel,
                        navController
                    )
                }
            }
        }
    }
}

private fun determineStartDestination(currentUser: User?): String {
    return when {
        currentUser == null -> "login_screen"
        currentUser.admin -> "list_of_sites"
        else -> "nearby_list_of_sites"
    }
}