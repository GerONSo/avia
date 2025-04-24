package com.serrriy.aviascan.main

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.serrriy.aviascan.R
import com.serrriy.aviascan.add_flight.AddFlightScreen
import com.serrriy.aviascan.add_flight.AddFlightViewModel
import com.serrriy.aviascan.add_flight.CameraScreen
import com.serrriy.aviascan.feed.FeedScreen
import com.serrriy.aviascan.flights.FlightsScreen
import com.serrriy.aviascan.profile.ProfileScreen
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.subscriptions.SubscriptionsScreen
import com.serrriy.aviascan.utils.Typography
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class MainScreen(@StringRes val title: Int) {
    Flights(title = R.string.flights),
    AddFlight(title = R.string.add_flight),
    Feed(title = R.string.feed),
    Profile(title = R.string.profile),
    Camera(title = R.string.camera),
    Subscriptions(title = R.string.subscriptions),
}

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Feed", MainScreen.Feed.name, Icons.Default.Feed),
    TopLevelRoute("Flights", MainScreen.Flights.name, Icons.Default.Flight),
    TopLevelRoute("Subscriptions", MainScreen.Subscriptions.name, Icons.Default.PersonSearch),
    TopLevelRoute("Profile", MainScreen.Profile.name, Icons.Default.Person),
)

@Composable
@Preview
fun App() {
    val viewModel: MainViewModel = rememberMainViewModel()
    val context = LocalContext.current
    val mainState by viewModel.mainState.collectAsState()

    LaunchedEffect(mainState.needRefresh) {
        if (mainState.needRefresh) {
            viewModel.initDataStore(context)
            viewModel.processAuth()
            viewModel.refresh(needRefresh = false)
        }
    }
    MaterialTheme(
        typography = Typography,
        content = {
            when (mainState.authStatus) {
                MainViewModel.AuthStatus.LOGGED_IN -> {
                    MainContent(viewModel)
                }

                MainViewModel.AuthStatus.LOGGED_OUT -> {
                    AuthorizationScreen(viewModel)
                }

                MainViewModel.AuthStatus.PROCESSING -> {
                    LoadingScreen()
                }
            }
        }
    )
}

@Composable
fun rememberMainViewModel(): MainViewModel {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(
                    dataStoreRepository = DataStoreRepository(context),
                ) as T
            }
        }
    }
    return viewModel(factory = factory)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
@Composable
fun MainContent(
    mainViewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        bottomBar = {
            NavigationBar(modifier = Modifier.imePadding()) {
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
            MainNavHost(navController, mainViewModel)
        }
    }
}

@Composable
fun rememberAddFlightViewModel(navController: NavHostController): AddFlightViewModel {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddFlightViewModel(
                    dataStoreRepository = DataStoreRepository(context),
                    navController = navController,
                ) as T
            }
        }
    }
    return viewModel(factory = factory)
}

@Composable
fun MainNavHost(navController: NavHostController, mainViewModel: MainViewModel) {
    val addFlightViewModel: AddFlightViewModel = rememberAddFlightViewModel(navController)

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
        composable(route = MainScreen.Profile.name) {
            ProfileScreen(mainViewModel = mainViewModel)
        }
        composable(route = MainScreen.AddFlight.name) {
            AddFlightScreen(navController, addFlightViewModel)
        }
        composable(route = MainScreen.Camera.name) {
            CameraScreen(navController, addFlightViewModel)
        }
        composable(route = MainScreen.Subscriptions.name) {
            SubscriptionsScreen()
        }
    }
}
