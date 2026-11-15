package com.example.gymnotes.ui.working

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gymnotes.R
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.model.TimerMode
import com.example.gymnotes.presentation.working.GymEffects
import com.example.gymnotes.presentation.working.GymInfo
import com.example.gymnotes.presentation.working.GymIntent
import com.example.gymnotes.presentation.working.GymViewModel
import com.example.gymnotes.ui.components.AddDetailsGymnasticDialog
import com.example.gymnotes.ui.components.AddGymnasticDialogForCategory
import com.example.gymnotes.ui.components.ButtonsLib
import com.example.gymnotes.ui.components.RemoveDialog
import com.example.gymnotes.ui.components.RenameListDialog
import com.example.gymnotes.util.FabItem
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymScreen(
    gymViewModel: GymViewModel = koinViewModel()
) {
    val state by gymViewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val fabItems = listOf(
        FabItem(
            text = stringResource(R.string.hands),
            iconRes = R.drawable.ic_hand_48_px
        ),
        FabItem(
            text = stringResource(R.string.chest),
            iconRes = R.drawable.ic_chest_48_px
        ),
        FabItem(
            text = stringResource(R.string.back),
            iconRes = R.drawable.ic_back_48_px
        ),
        FabItem(
            text = stringResource(R.string.legs),
            iconRes = R.drawable.ic_leg_48_px
        )
    )

    val selectedIndex = fabItems.indexOfFirst { it.text == state.categoryForWatching }
        .takeIf { it != -1 } ?: 0

    LaunchedEffect(key1 = Unit) {
        gymViewModel.effect.collect { effect ->
            when(effect) {
                is GymEffects.ShowInfo -> {
                    val message = when(effect.info) {
                        is GymInfo.AddGymError -> context.getString(R.string.add_gymnastic_error)
                        is GymInfo.RemoveError -> context.getString(R.string.remove_gymnastic_error)
                        is GymInfo.RenameError -> context.getString(R.string.rename_gymnastic_error)
                        is GymInfo.LoadError -> context.getString(R.string.get_gymnastics_error)
                        is GymInfo.AddNoteError -> context.getString(R.string.add_note_error)
                        is GymInfo.GymNameEmpty -> context.getString(R.string.gym_name_empty)
                        is GymInfo.GymNameOver -> context.getString(R.string.gym_name_over)
                        is GymInfo.RepsEmpty -> context.getString(R.string.reps_empty_error)
                        is GymInfo.RepsZero -> context.getString(R.string.reps_null_error)
                        is GymInfo.RepsOver -> context.getString(R.string.reps_max_error)
                        is GymInfo.HeftEmpty -> context.getString(R.string.heft_empty_error)
                        is GymInfo.HeftZero -> context.getString(R.string.heft_null_error)
                        is GymInfo.HeftOver -> context.getString(R.string.heft_max_error)
                        is GymInfo.SuccessAddGym -> context.getString(R.string.add_gymnastic_success, state.categoryForCreating)
                        is GymInfo.SuccessRemoveGym -> context.getString(R.string.remove_gymnastic_success, state.categoryForCreating)
                        is GymInfo.SuccessRenameGym -> context.getString(R.string.rename_gymnastic_success)
                        is GymInfo.SuccessAddNote -> context.getString(R.string.add_note_success)
                        is GymInfo.Success -> {}
                    }
                    snackbarHostState.showSnackbar(
                        message = message.toString(),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0,0,0,0),
                title = {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = if (state.isTimerActive) stringResource(R.string.timer) else stringResource(R.string.choose_gym),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = { gymViewModel.handleIntent(GymIntent.ToggleTimerActive) },
                        modifier = Modifier.padding(
                            start = 4.dp,
                            top = 12.dp,
                            bottom = 12.dp,
                            end = 4.dp
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.sharp_timer_24),
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
            if (!state.isTimerActive) {
                ButtonsLib.FabMenu(
                    iconIdAdd = R.drawable.baseline_add_24,
                    iconIdClose = R.drawable.round_close_24,
                    onCategorySelected = { categoryText ->
                        gymViewModel.handleIntent(GymIntent.ShowAddGymDialogForCategory(categoryText)) },
                    fabItems = fabItems
                )
            }
        },
        snackbarHost = {
            Box {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.Center),
                        snackbarData = data,
                        shape = MaterialTheme.shapes.medium,
                        containerColor = if (
                            state.validation == GymInfo.Success ||
                            state.validation == GymInfo.SuccessAddGym ||
                            state.validation == GymInfo.SuccessRenameGym ||
                            state.validation == GymInfo.SuccessRemoveGym ||
                            state.validation == GymInfo.SuccessAddNote
                        ) {
                            Color.Green
                        } else {
                            Color.Red
                        },
                        contentColor = Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (!state.isTimerActive) {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.padding(8.dp)) {
                    fabItems.forEachIndexed { index, item ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = fabItems.size),
                            selected = index == selectedIndex,
                            onClick = {
                                gymViewModel.handleIntent(GymIntent.ChooseWatchingCategory(item.text))
                            },
                            label = { Text(item.text) }
                        )
                    }
                }
                if (gymViewModel.filteredGymItems.isEmpty()) {
                    ShowEmptyGymnasticsPlaceHolder()
                } else {
                    ShowGymnasticsContent(
                        listOfGymnastic = gymViewModel.filteredGymItems,
                        onItemClick = { gymnastic ->
                            gymViewModel.handleIntent(GymIntent.SelectGym(gymnastic))
                            gymViewModel.handleIntent(GymIntent.ShowAddGymDetailsDialog)
                        },
                        removeGym = { gymnastic ->
                            gymViewModel.handleIntent(GymIntent.SelectGym(gymnastic))
                            gymViewModel.handleIntent(GymIntent.ShowRemoveGymDialog)
                        },
                        renameGym = { gymnastic ->
                            gymViewModel.handleIntent(GymIntent.SelectGym(gymnastic))
                            gymViewModel.handleIntent(GymIntent.SetNewName(gymnastic.name))
                            gymViewModel.handleIntent(GymIntent.ShowRenameGymDialog)
                        }
                    )
                }
            } else {
                ShowTimer(
                    time = state.currentTime,
                    timerMode = state.timerMode,
                    onToggle = { gymViewModel.toggleTimer() },
                    onReset = { gymViewModel.resetTimer() }
                )
            }
        }
        AddGymnasticDialogForCategory(
            visible = state.isAddGymDialogVisible,
            validationMessage = when(state.validation) {
                is GymInfo.GymNameEmpty -> stringResource(R.string.gym_name_empty)
                is GymInfo.GymNameOver -> stringResource(R.string.gym_name_over)
                else -> {""}
            },
            onDismissRequest = {
                gymViewModel.handleIntent(GymIntent.HideAddGymDialog)
                gymViewModel.handleIntent(GymIntent.DefaultValidation)
           },
            onConfirmation = { gymnasticName ->
                gymViewModel.handleIntent(GymIntent.AddGym(
                    category = state.categoryForCreating,
                    gymnastic = gymnasticName
                ))
            },
            onDismission = {
                gymViewModel.handleIntent(GymIntent.HideAddGymDialog)
                gymViewModel.handleIntent(GymIntent.DefaultValidation)
            },
        )
        AddDetailsGymnasticDialog(
            visible = state.isAddGymDetailsDialogVisible,
            validationMessage = when(state.validation) {
                is GymInfo.RepsEmpty -> stringResource(R.string.reps_empty_error)
                is GymInfo.RepsZero -> stringResource(R.string.reps_null_error)
                is GymInfo.RepsOver -> stringResource(R.string.reps_max_error)
                is GymInfo.HeftEmpty -> stringResource(R.string.heft_empty_error)
                is GymInfo.HeftZero -> stringResource(R.string.heft_null_error)
                is GymInfo.HeftOver -> stringResource(R.string.heft_max_error)
                else -> {""}
            },
            onDismissRequest = {
                gymViewModel.handleIntent(GymIntent.HideAddGymDetailsDialog)
                gymViewModel.handleIntent(GymIntent.DefaultValidation)
            },
            onConfirmation = { reps, heft ->
                gymViewModel.handleIntent(
                    GymIntent.AddNote(
                        reps = reps,
                        heft = heft,
                        gym = state.currentGymnastic!!
                    )
                )
            }
        )
        RenameListDialog(
            visible = state.isRenameGymDialogVisible,
            onDismissRequest = { gymViewModel.handleIntent(GymIntent.HideRenameGymDialog) },
            onConfirmation = { newName ->
                gymViewModel.handleIntent(GymIntent.RenameGym(
                    id = state.currentGymnastic!!.id,
                    newName = newName
                    )
                )
                gymViewModel.handleIntent(GymIntent.HideRenameGymDialog)
            },
            onDismission = { gymViewModel.handleIntent(GymIntent.HideRenameGymDialog) },
            name = state.newGymName,
            onNameChange = { text ->
                gymViewModel.handleIntent(GymIntent.SetNewName(text))
            }
        )
        RemoveDialog(
            visible = state.isRemoveGymDialogVisible,
            onDismissRequest = { gymViewModel.handleIntent(GymIntent.HideRemoveGymDialog) },
            onConfirmation = {
                state.currentGymnastic.let {
                    gymViewModel.handleIntent(GymIntent.RemoveGym(it!!.id))
                }
                gymViewModel.handleIntent(GymIntent.HideRemoveGymDialog)
            },
            onDismission = { gymViewModel.handleIntent(GymIntent.HideRemoveGymDialog) },
            title = stringResource(R.string.remove_gym)
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
            text = stringResource(R.string.empty_list),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.empty_gymnastics_suggest),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.placeholder_empty),
            contentDescription = null
        )
    }
}

@Composable
fun ShowGymnasticsContent(
    listOfGymnastic: List<Gymnastic>,
    onItemClick: (Gymnastic) -> Unit,
    removeGym: (Gymnastic) -> Unit,
    renameGym: (Gymnastic) -> Unit
) {
    LazyColumn {
        items(
            items = listOfGymnastic,
            key = { gym -> gym.id }
        ) {  gymnastic ->
            val swipeState = rememberSwipeToDismissBoxState(
                initialValue = SwipeToDismissBoxValue.Settled,
                confirmValueChange = { direction ->
                    when(direction) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            removeGym(gymnastic)
                            false
                        }
                        SwipeToDismissBoxValue.EndToStart -> {
                            renameGym(gymnastic)
                            false
                        }
                        else -> false
                    }
                },
                positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
            )

            SwipeToDismissBox(
                state = swipeState,
                backgroundContent = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_delete_24),
                            contentDescription = stringResource(R.string.remove),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.outline_edit_24),
                            contentDescription = stringResource(R.string.rename),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enableDismissFromStartToEnd = true,
                enableDismissFromEndToStart = true,
                gesturesEnabled = true
            ) {
                GymnasticItem(
                    index = (listOfGymnastic.indexOf(gymnastic) + 1).toString(),
                    gymnastic = gymnastic,
                    onItemClick = { onItemClick(gymnastic) }
                )
            }

        }
    }
}

@Composable
fun GymnasticItem(
    index: String,
    gymnastic: Gymnastic,
    onItemClick: (Gymnastic) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
        onClick = { onItemClick(gymnastic) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = index + ". " + gymnastic.name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ShowTimer(
    time: String,
    timerMode: TimerMode,
    onToggle: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onToggle() },
                    onLongClick = { onReset() }
                ),
            text = time,
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(50.dp))
        ButtonsLib.Button(
            onClick = { onToggle() },
            stringId = if (timerMode is TimerMode.Work) R.string.pause_timer else R.string.start_timer
        )
        ButtonsLib.Button(
            onClick = { onReset() },
            stringId = R.string.reset_timer
        )
    }
}