package com.example.gymnotes.ui.working

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import com.example.gymnotes.R
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.presentation.working.GymEffects
import com.example.gymnotes.presentation.working.GymIntent
import com.example.gymnotes.presentation.working.GymViewModel
import com.example.gymnotes.ui.components.AddDetailsGymnasticDialog
import com.example.gymnotes.ui.components.AddGymnasticDialogForCategory
import com.example.gymnotes.ui.components.ButtonsLib
import com.example.gymnotes.ui.components.RemoveDialog
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

    LaunchedEffect(key1 = state.categoryForWatching) {
        gymViewModel.handleIntent(GymIntent.LoadGymsByCategory(state.categoryForWatching))
    }

    LaunchedEffect(key1 = Unit) {
        gymViewModel.effect.collect { effect ->
            when(effect) {
                is GymEffects.ShowToast -> {
                    Toast.makeText(context, effect.error.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0,0,0,0),
                title = {
                    Column(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.choose_gym),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SingleChoiceSegmentedButtonRow {
                            fabItems.forEachIndexed { index, item ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = fabItems.size),
                                    selected = index == selectedIndex,
                                    onClick = {
                                        gymViewModel.handleIntent(GymIntent.LoadGymsByCategory(item.text))
                                    },
                                    label = { Text(item.text) }
                                )
                            }
                        }
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ButtonsLib.FabMenu(
                iconIdAdd = R.drawable.outline_add_24,
                iconIdClose = R.drawable.ic_close_800_px,
                onCategorySelected = { categoryText ->
                    gymViewModel.handleIntent(GymIntent.ShowAddGymDialogForCategory(categoryText)) },
                fabItems = fabItems
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            if (state.gymItems.isEmpty()) {
                ShowEmptyGymnasticsPlaceHolder()
            } else {
                ShowGymnasticsContent(
                    listOfGymnastic = state.gymItems,
                    onItemClick = { gymnastic ->
                        gymViewModel.handleIntent(GymIntent.SelectGym(gymnastic))
                        gymViewModel.handleIntent(GymIntent.ShowAddGymDetailsDialog)
                    },
                    onItemLongClick = { gymnastic ->
                        gymViewModel.handleIntent(GymIntent.SelectGym(gymnastic))
                        gymViewModel.handleIntent(GymIntent.ShowRemoveGymDialog)
                    }
                )
            }
        }
        AddGymnasticDialogForCategory(
            visible = state.isAddGymDialogVisible,
            onDismissRequest = { gymViewModel.handleIntent(GymIntent.HideAddGymDialog) },
            onConfirmation = { gymnasticName ->
                if (gymnasticName.isNotBlank()) {
                    gymViewModel.handleIntent(GymIntent.AddGym(
                        category = state.categoryForCreating,
                        gymnastic = gymnasticName
                    ))
                }
            },
            onDismission = { gymViewModel.handleIntent(GymIntent.HideAddGymDialog) }
        )
        AddDetailsGymnasticDialog(
            visible = state.isAddGymDetailsDialogVisible,
            onDismissRequest = { gymViewModel.handleIntent(GymIntent.HideAddGymDetailsDialog) },
            onConfirmation = { reps, heft ->
                if (reps.isNotBlank() && heft.isNotBlank()) {
                    gymViewModel.handleIntent(GymIntent.AddNote(
                        reps = reps,
                        heft = heft,
                        gym = state.currentGymnastic
                    ))
                }
                gymViewModel.handleIntent(GymIntent.HideAddGymDetailsDialog)
            },
            onDismission = { gymViewModel.handleIntent(GymIntent.HideAddGymDetailsDialog) }
        )
        RemoveDialog(
            visible = state.isRemoveGymDialogVisible,
            onDismissRequest = { gymViewModel.handleIntent(GymIntent.HideRemoveGymDialog) },
            onConfirmation = {
                state.currentGymnastic.let {
                    gymViewModel.handleIntent(GymIntent.RemoveGym(it.id))
                }
                gymViewModel.handleIntent(GymIntent.HideRemoveGymDialog)
            },
            onDismission = { gymViewModel.handleIntent(GymIntent.HideRemoveGymDialog) },
            title = state.categoryForWatching
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