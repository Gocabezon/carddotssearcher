
package com.example.carddotsearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carddotsearcher.ui.LoginScreen
import com.example.carddotsearcher.ui.theme.CarddotsearcherTheme
import com.example.carddotsearcher.viewmodel.MainViewModel
import com.example.carddotsearcher.ui.CameraContainer
import com.example.carddotsearcher.ui.ResultsScreen
import com.example.carddotsearcher.ui.HistoryScreen
import com.example.carddotsearcher.ui.PrincipalScreen
import com.example.carddotsearcher.ui.RegistroScreen
import com.example.carddotsearcher.ui.theme.GPS
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarddotsearcherTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "principal",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("principal") {
                            PrincipalScreen(navController = navController)
                        }
                        composable("login") {
                            LoginScreen(navController = navController)
                        }
                        composable("registro") {
                            RegistroScreen(navController = navController)
                        }
                        composable("camera") {
                            CameraContainer(navController = navController, viewModel = mainViewModel)
                        }
                        composable("results") {
                            ResultsScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable("history") {
                            HistoryScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable("gps"){
                            GPS(navController = navController)
                        }
                    }
                }
            }
        }
    }
}