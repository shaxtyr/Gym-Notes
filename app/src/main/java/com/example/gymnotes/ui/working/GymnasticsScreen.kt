package com.example.gymnotes.ui.working

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.gymnotes.presentation.working.GymnasticsEffects
import com.example.gymnotes.presentation.working.GymnasticsIntent
import com.example.gymnotes.presentation.working.GymnasticsViewModel
import com.example.gymnotes.ui.components.AddDetailsGymnasticDialog
import com.example.gymnotes.ui.components.AddGymnasticDialog
import com.example.gymnotes.ui.components.ButtonsLib
import com.example.gymnotes.ui.components.RemoveDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymnasticsScreen(
    category: String,
    navController: NavController,
    gymnasticViewModel: GymnasticsViewModel = koinViewModel()
) {
    val state by gymnasticViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = category) {
        gymnasticViewModel.handleIntent(GymnasticsIntent.LoadGymnasticsByCategory(category))
    }

    LaunchedEffect(key1 = Unit) {
        gymnasticViewModel.effect.collect { effect ->
            when(effect) {
                is GymnasticsEffects.ShowToast -> {
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
                        text = stringResource(R.string.gymnastic_choice),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                            horizontal = 4.dp
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_24px),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ButtonsLib.FloatingActionButton(
                onClick = { gymnasticViewModel.handleIntent(GymnasticsIntent.ShowAddGymnasticDialog) },
                iconId = R.drawable.ic_add_gym_100px,
                stringId = R.string.dialog_adding_gymnastic
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            if (state.gymnasticItems.isEmpty()) {
                ShowEmptyGymnasticsPlaceHolder()
            } else {
                ShowGymnasticsContent(
                    listOfGymnastic = state.gymnasticItems,
                    onItemClick = { gymnastic ->
                        gymnasticViewModel.handleIntent(GymnasticsIntent.SelectGymnastic(gymnastic))
                        gymnasticViewModel.handleIntent(GymnasticsIntent.ShowAddDetailsGymnasticDialog)
                    },
                    onItemLongClick = { gymnastic ->
                        gymnasticViewModel.handleIntent(GymnasticsIntent.SelectGymnastic(gymnastic))
                        gymnasticViewModel.handleIntent(GymnasticsIntent.ShowRemoveGymnasticDialog)
                    }
                )
            }
        }
        AddGymnasticDialog(
            visible = state.isAddDialogVisible,
            onDismissRequest = { gymnasticViewModel.handleIntent(GymnasticsIntent.HideAddGymnasticDialog) },
            onConfirmation = { gymnasticName ->
                if (gymnasticName.isNotBlank()) {
                    gymnasticViewModel.handleIntent(GymnasticsIntent.AddGymnastic(
                        category = category,
                        gymnastic = gymnasticName
                    ))
                }
            },
            onDismission = { gymnasticViewModel.handleIntent(GymnasticsIntent.HideAddGymnasticDialog) }
        )
        AddDetailsGymnasticDialog(
            visible = state.isAddDetailsDialogVisible,
            onDismissRequest = { gymnasticViewModel.handleIntent(GymnasticsIntent.HideAddDetailsGymnasticDialog) },
            onConfirmation = { reps, heft ->
                if (reps.isNotBlank() && heft.isNotBlank()) {
                    gymnasticViewModel.handleIntent(GymnasticsIntent.AddNote(
                        reps = reps,
                        heft = heft,
                        gymnastic = state.currentGymnastic
                    ))
                }
                gymnasticViewModel.handleIntent(GymnasticsIntent.HideAddDetailsGymnasticDialog)
            },
            onDismission = { gymnasticViewModel.handleIntent(GymnasticsIntent.HideAddDetailsGymnasticDialog) }
        )
        RemoveDialog(
            visible = state.isRemoveDialogVisible,
            onDismissRequest = { gymnasticViewModel.handleIntent(GymnasticsIntent.HideRemoveGymnasticDialog) },
            onConfirmation = {
                state.currentGymnastic.let {
                    gymnasticViewModel.handleIntent(GymnasticsIntent.RemoveGymnastic(it.id))
                }
                gymnasticViewModel.handleIntent(GymnasticsIntent.HideRemoveGymnasticDialog)
            },
            onDismission = { gymnasticViewModel.handleIntent(GymnasticsIntent.HideRemoveGymnasticDialog) },
            title = category
        )
    }
}

@Composable
fun ShowEmptyGymnasticsPlaceHolder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.empty_gymnastics),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.placeholder_empty_gymnastic_512px),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_gymnastics_suggest),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun ShowGymnasticsContent(
    listOfGymnastic: List<Gymnastic>,
    onItemClick: (Gymnastic) -> Unit,
    onItemLongClick: (Gymnastic) -> Unit
) {
    LazyColumn {
        items(items = listOfGymnastic) { gymnastic ->
            GymnasticItem(
                gymnastic = gymnastic,
                onItemClick = { onItemClick(gymnastic) },
                onItemLongClick = { onItemLongClick(gymnastic) }
            )
        }
    }
}

@Composable
fun GymnasticItem(
    gymnastic: Gymnastic,
    onItemClick: (Gymnastic) -> Unit,
    onItemLongClick: (Gymnastic) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
            .combinedClickable(
                onClick = { onItemClick(gymnastic) },
                onLongClick = { onItemLongClick(gymnastic) }
            ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = gymnastic.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}