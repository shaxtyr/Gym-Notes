package com.example.gymnotes.ui.working

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymnotes.R
import com.example.gymnotes.presentation.working.StartWorkingEffect
import com.example.gymnotes.presentation.working.StartWorkingIntent
import com.example.gymnotes.presentation.working.StartWorkingViewModel
import com.example.gymnotes.ui.navigation.Routes
import org.koin.androidx.compose.koinViewModel
enum class GymCategory(val resId: Int, val key: String) {
    HANDS(R.string.hands, "hands"),
    CHEST(R.string.chest, "chest"),
    BACK(R.string.back, "back"),
    LEGS(R.string.legs, "legs")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartWorkingScreen(
    navController: NavController,
    viewModel: StartWorkingViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                is StartWorkingEffect.NavigateToGymnasticsScreen -> {
                    navController.navigate("${Routes.Gymnastics.name}/${effect.category}")
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(text = stringResource(R.string.category_choice))
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
                .padding(innerPadding)
        ) {
            GymCategory.values().forEach { category ->
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        viewModel.handleIntent(StartWorkingIntent.ChooseCategory(category = category.key))
                    }
                ) {
                    Text(
                        textAlign = TextAlign.Start,
                        text = stringResource(category.resId),
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }
        }
    }
}



/*
Button(
onClick = {
    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    viewModel.handleIntent(StartWorkingIntent.Load(
        Note(
            id = UUID.randomUUID().toString(),
            sets = "5",
            reps = "3",
            heft = "200",
            exercise = "Gym",
            category = "Legs",
            addedAt = currentDate,
            monthYear = DateUtils.getMonthYear(DateUtils.parseIsoStringToLocalDate(currentDate)),
            day = DateUtils.getDay(DateUtils.parseIsoStringToLocalDate(currentDate)),
            dayOfWeek = DateUtils.getDayOfWeek(DateUtils.parseIsoStringToLocalDate(currentDate))
        )
    )) }
) { }
*/
/*
@Composable
fun StartWorkingScreen(
    navController: NavController,
    viewModel: StartWorkingViewModel = koinViewModel()
) {
    val categories = listOf(
        "Руки", "Грудь", "Спина", "Ноги"
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            Card(
                modifier = Modifier.fillMaxSize(),
                onClick = {}
            ) {
                Text(
                    text = category,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}*/