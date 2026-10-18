package com.example.gymnotes.ui.navigation

import com.example.gymnotes.R

data class BottomNavigationItem(
    val route: String,
    val iconResource: Int,
    val titleResource: Int
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        Routes.NotesDate.name,
        R.drawable.ic_notes_24_px,
        R.string.notes
    ),
    BottomNavigationItem(
        Routes.Gym.name,
        R.drawable.ic_start_working_24_px,
        R.string.start_working
    ),
    BottomNavigationItem(
        Routes.Achievements.name,
        R.drawable.ic_achievements_24_px,
        R.string.achievements
    )
)