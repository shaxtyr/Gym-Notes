package com.example.gymnotes.ui.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gymnotes.ui.navigation.NavGraph
import com.example.gymnotes.ui.theme.GymNotesTheme

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymNotesTheme {
                NavGraph()
            }
        }
    }
}
