package com.example.gymnotes.ui.notes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymnotes.R
import com.example.gymnotes.presentation.notes.NotesEffect
import com.example.gymnotes.presentation.notes.NotesIntent
import com.example.gymnotes.presentation.notes.NotesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    addedAt: String,
    navController: NavController,
    notesViewModel: NotesViewModel = koinViewModel()
) {
    val state by notesViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = addedAt) {
        notesViewModel.handleIntent(NotesIntent.LoadNotes(addedAt))
    }

    LaunchedEffect(key1 = Unit) {
        notesViewModel.effect.collect { effect ->
            when(effect) {
                is NotesEffect.ShowToast -> {
                    Toast.makeText(context, effect.error.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        text = stringResource(R.string.notes_by_date, addedAt),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ShowNotes(state.notesItems)
        }
    }
}