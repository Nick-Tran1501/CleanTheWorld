package com.example.cleantheworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cleantheworld.activities.HomeScreenActivity
import com.example.cleantheworld.activities.LoginScreenActivity
import com.example.cleantheworld.activities.RegisterScreenActivity
import com.example.cleantheworld.ui.theme.AppTheme
import com.example.cleantheworld.utils.ThemeViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
            // After the delay, set the content view to your main layout
            setContent {
                val themeViewModel: ThemeViewModel = viewModel()

                // Observe the theme state
                val isDarkTheme = themeViewModel.isDarkTheme.value
                AppTheme(useDarkTheme = isDarkTheme) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainNavigationActivity()
                    }
                }
            }

    }
}

@Composable
fun MainNavigationActivity(){
    val navController = rememberNavController()
    val themeViewModel: ThemeViewModel = viewModel()
    NavHost(navController = navController, startDestination = "login_screen"){
        composable("login_screen"){
            LoginScreenActivity(navController, themeViewModel)
        }
        composable("register_screen"){
            RegisterScreenActivity(navController, themeViewModel)
        }
        composable("home_screen"){
            HomeScreenActivity(themeViewModel)
        }

    }
}