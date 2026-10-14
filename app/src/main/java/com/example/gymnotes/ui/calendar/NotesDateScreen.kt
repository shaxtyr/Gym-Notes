package com.example.gymnotes.ui.calendar

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.model.NotesDateGrid
import com.example.gymnotes.presentation.notes.NotesDateEffect
import com.example.gymnotes.presentation.notes.NotesDateIntent
import com.example.gymnotes.presentation.notes.NotesDateViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesDateScreen(
    onNavigateToNotes: (String) -> Unit,
    viewModel: NotesDateViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NotesDateEffect.NavigateToNotes -> onNavigateToNotes(effect.addedAt)
                is NotesDateEffect.ShowToast -> {
                    Toast.makeText(context, effect.error.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            windowInsets = WindowInsets(0, 0, 0, 0),
            title = {
                Text(
                    text = stringResource(R.string.notes),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
        if (state.gridNotesDateItems.isEmpty()) {
            ShowEmptyNotesPlaceHolder()
        } else {
            ShowDateNotesContent(
                gridNoteItems = state.gridNotesDateItems,
                onItemClick = { addedAt ->
                    viewModel.handleIntent(NotesDateIntent.ShowNotes(addedAt))
                },
                onItemLongClick = { addedAt ->
                    viewModel.handleIntent(NotesDateIntent.RemoveNotesDate(addedAt))
                }
            )
        }
    }
}

@Composable
fun ShowEmptyNotesPlaceHolder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.empty_notes),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.placeholder_empty_note_512px),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_notes_suggest),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
@Composable
fun ShowDateNotesContent(
    gridNoteItems: List<NotesDateGrid>,
    onItemClick: (String) -> Unit,
    onItemLongClick: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(4)
    ) {
        items(
            items = gridNoteItems,
            span = { item ->
                when(item) {
                    is NotesDateGrid.HeaderNotesDate -> {
                        GridItemSpan(4)
                    }
                    is NotesDateGrid.ContentNotesDate -> {
                        GridItemSpan(1)
                    }
                }
            }
        ) { item ->
                when(item) {
                    is NotesDateGrid.HeaderNotesDate -> NoteDateHeaderItem(item.monthYear)
                    is NotesDateGrid.ContentNotesDate -> NoteDateContentItem(
                        dayNote = item.day,
                        dayOfWeekNote = item.dayOfWeek,
                        onItemClick = { onItemClick(item.addedAt) },
                        onItemLongClick = { onItemLongClick(item.addedAt) },
                    )
                }
        }
    }
}