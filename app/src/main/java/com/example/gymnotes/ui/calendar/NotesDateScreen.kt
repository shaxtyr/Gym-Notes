package com.example.gymnotes.ui.calendar

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymnotes.R
import com.example.gymnotes.domain.model.NotesDateGridItem
import com.example.gymnotes.presentation.notes.NotesDateEffect
import com.example.gymnotes.presentation.notes.NotesDateIntent
import com.example.gymnotes.presentation.notes.NotesDateViewModel
import com.example.gymnotes.presentation.working.GymIntent
import com.example.gymnotes.ui.components.RemoveDialog
import com.example.gymnotes.ui.theme.GymNotesTheme
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
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        text = stringResource(R.string.notes),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(
                        onClick = { viewModel.handleIntent(NotesDateIntent.ToggleTheme) },
                        modifier = Modifier.padding(
                            start = 4.dp,
                            top = 12.dp,
                            bottom = 12.dp,
                            end = 4.dp
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.choose_theme_24),
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = { viewModel.handleIntent(NotesDateIntent.ShowHelp) },
                        modifier = Modifier.padding(
                            start = 4.dp,
                            top = 12.dp,
                            bottom = 12.dp,
                            end = 4.dp
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_help_24),
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            if (state.gridNotesDateItems.isEmpty()) {
                ShowEmptyNotesPlaceHolder()
            } else {
                ShowDateNotesContent(
                    gridNoteItems = state.gridNotesDateItems,
                    onItemClick = { addedAt ->
                        viewModel.handleIntent(NotesDateIntent.ShowNotes(addedAt))
                    },
                    onItemLongClick = { addedAt ->
                        viewModel.handleIntent(NotesDateIntent.SelectDate(addedAt))
                        viewModel.handleIntent(NotesDateIntent.ShowRemoveDateDialog)
                        //viewModel.handleIntent(NotesDateIntent.RemoveNotesDate(addedAt))
                    }
                )
            }
        }
        RemoveDialog(
            visible = state.isRemoveDateDialogVisible,
            onDismissRequest = { viewModel.handleIntent(NotesDateIntent.HideRemoveDateDialog) },
            onConfirmation = {
                state.dateToRemove.let {
                    viewModel.handleIntent(NotesDateIntent.RemoveNotesDate(it))
                }
                viewModel.handleIntent(NotesDateIntent.HideRemoveDateDialog)
            },
            onDismission = { viewModel.handleIntent(NotesDateIntent.HideRemoveDateDialog) },
            title = stringResource(R.string.remove_list_of, state.dateToRemove)
        )
    }
}
@Composable
fun ShowEmptyNotesPlaceHolder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.placeholder_empty),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.empty_list),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.empty_notes_suggest),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}
@Composable
fun ShowDateNotesContent(
    gridNoteItems: List<NotesDateGridItem>,
    onItemClick: (String) -> Unit,
    onItemLongClick: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(4)
    ) {
        items(
            items = gridNoteItems,
            span = { item ->
                when(item) {
                    is NotesDateGridItem.HeaderNotesDateItem -> {
                        GridItemSpan(4)
                    }
                    is NotesDateGridItem.ContentNotesDateItem -> {
                        GridItemSpan(1)
                    }
                }
            }
        ) { item ->
                when(item) {
                    is NotesDateGridItem.HeaderNotesDateItem -> NoteDateHeaderItem(item.monthYear)
                    is NotesDateGridItem.ContentNotesDateItem -> NoteDateContentItem(
                        dayNote = item.day,
                        dayOfWeekNote = item.dayOfWeek,
                        onItemClick = { onItemClick(item.addedAt) },
                        onItemLongClick = { onItemLongClick(item.addedAt) },
                    )
                }
        }
    }
}