package com.example.gymnotes.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymnotes.ui.achievements.AchievementsScreen
import com.example.gymnotes.ui.notes.NotesScreen
import com.example.gymnotes.ui.calendar.NotesDateScreen
import com.example.gymnotes.ui.working.GymScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = Routes.NotesDate.name
        ) {
            composable(Routes.NotesDate.name) { NotesDateScreen(onNavigateToNotes = { addedAt ->
                navController.navigate("${Routes.Notes.name}/$addedAt")
            }) }
            composable(Routes.Achievements.name) { AchievementsScreen(navController) }
            composable(Routes.Gym.name) { GymScreen() }
            composable(
                route = "${Routes.Notes.name}/{addedAt}",
                arguments = listOf(
                    navArgument(
                        name = "addedAt",
                        builder = { type = NavType.StringType }
                    )
                )
            ) { backStackEntry ->
                val addedAt = backStackEntry.arguments?.getString("addedAt") ?: ""
                NotesScreen(addedAt, navController)
            }
        }
    }
}