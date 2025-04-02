package com.serrriy.aviascan

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.serrriy.aviascan.achievements.AchievementsScreen
import com.serrriy.aviascan.add_flight.AddFlightScreen
import com.serrriy.aviascan.add_flight.AddFlightViewModel
import com.serrriy.aviascan.add_flight.CameraScreen
import com.serrriy.aviascan.feed.FeedScreen
import com.serrriy.aviascan.flights.FlightsScreen
import com.serrriy.aviascan.utils.Typography
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class MainScreen(@StringRes val title: Int) {
    Flights(title = R.string.flights),
    AddFlight(title = R.string.add_flight),
    Feed(title = R.string.feed),
    Achievements(title = R.string.achievements),
    Camera(title = R.string.camera),
}

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Feed", MainScreen.Feed.name, Icons.Default.Feed),
    TopLevelRoute("Flights", MainScreen.Flights.name, Icons.Default.Flight),
    TopLevelRoute("Achievements", MainScreen.Achievements.name, Icons.Default.Star),
)

@Composable
@Preview
fun App() {
    MaterialTheme(
        typography = Typography,
        content = { MainContent() }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
@Composable
fun MainContent(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                topLevelRoutes.forEach { topLevelRoute ->
                    NavigationBarItem(
                        icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                        label = { Text(topLevelRoute.name) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route, null) } == true,
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).background(Color.White),
        ) {
            MainNavHost(navController)
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController) {
    val addFlightViewModel: AddFlightViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = MainScreen.Feed.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = MainScreen.Flights.name) {
            FlightsScreen(navController)
        }
        composable(route = MainScreen.Feed.name) {
            FeedScreen()
        }
        composable(route = MainScreen.Achievements.name) {
            AchievementsScreen()
        }
        composable(route = MainScreen.AddFlight.name) {
            AddFlightScreen(navController, addFlightViewModel)
        }
        composable(route = MainScreen.Camera.name) {
            CameraScreen(navController, addFlightViewModel)
        }
    }
}
