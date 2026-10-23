package com.example.gymnotes.ui.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gymnotes.R
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.presentation.achievements.AchievementsIntent
import com.example.gymnotes.presentation.achievements.AchievementsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    achievementsViewModel: AchievementsViewModel = koinViewModel()
) {
    val state by achievementsViewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        achievementsViewModel.handleIntent(AchievementsIntent.ShowListOfAchievements)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        text = stringResource(R.string.achievements),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.listOfAchievements.isEmpty()) {
                ShowPlaceholderAchievements()
            } else {
                ShowAchievements(state.listOfAchievements)
            }
        }
    }
}
@Composable
fun ShowPlaceholderAchievements() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.empty_gymnastics),
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
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun ShowAchievements(
    listOfAchievements: List<Note>
) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(listOfAchievements) { achievement ->
            MakeTitleRow(
                gymName = achievement.gymnasticName,
                category = achievement.category
            )
            MakeDataRow(
                maxHeft = achievement.heft,
                reps = achievement.reps
            )
        }
    }
}

@Composable
fun MakeTitleRow(
    gymName: String,
    category: String
) {
    val icon = when(category) {
        stringResource(R.string.hands) -> R.drawable.ic_hand_48_px
        stringResource(R.string.chest) -> R.drawable.ic_chest_48_px
        stringResource(R.string.back) -> R.drawable.ic_back_48_px
        else -> R.drawable.ic_leg_48_px
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = gymName,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.heft_max),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.reps),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun MakeDataRow(
    maxHeft: String,
    reps: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = maxHeft,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = reps,
            style = MaterialTheme.typography.titleLarge
        )
    }
}