package com.example.barta

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.compose.ui.unit.dp
import com.example.barta.ui.theme.LocalBartaPalette

@Composable
fun BartaAppMain() {
    val navController = rememberNavController()
    val color = LocalBartaPalette.current
    val items = listOf(
        NavigationBar.Dashboard,
        NavigationBar.Home,
        NavigationBar.Profile
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = color.backgroundGray1,
                modifier = Modifier.height(60.dp)
            ) {
                items.forEach { NavigationBar ->
                    val selected = navController.currentBackStackEntryAsState().value?.destination?.route == NavigationBar.route

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (selected) NavigationBar.selectedIcon else NavigationBar.icon
                                ),
                                contentDescription = NavigationBar.label,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = NavigationBar.label,
                                style = typography.caption
                            )
                        },
                        selected = selected,
                        selectedContentColor = color.primaryOrange,
                        unselectedContentColor = color.textGray2,
                        onClick = {
                            navController.navigate(NavigationBar.route) {
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
        NavHost(
            navController = navController,
            startDestination = NavigationBar.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationBar.Dashboard.route) {
                DashboardScreen(navController, modifier = Modifier.padding(innerPadding))
            }
            composable(NavigationBar.Home.route) {
                HomeScreen(navController, modifier = Modifier.padding(innerPadding))
            }
            composable(NavigationBar.Profile.route) {
                ProfileScreen(navController, modifier = Modifier.padding(innerPadding))
            }
            composable("player/{videoId}") { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
                PlayerScreen(videoId = videoId, navController = navController)
            }

        }
    }
}