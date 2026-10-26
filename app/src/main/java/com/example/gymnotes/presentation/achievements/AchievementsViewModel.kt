package com.example.gymnotes.presentation.achievements

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.presentation.BaseViewModel
import kotlinx.coroutines.flow.first

data class AchievementsState(
    val listOfAchievements: List<Note> = emptyList<Note>()
)

sealed class AchievementsIntent {
    data object ShowListOfAchievements: AchievementsIntent()
}
sealed class AchievementsError {
    object LoadFailed : AchievementsError()
}

sealed class AchievementsEffects {
    data class ShowToast(val error: AchievementsError) : AchievementsEffects()
}

class AchievementsViewModel(
    private val noteInteractor: NoteInteractor
): BaseViewModel<AchievementsState, AchievementsIntent, AchievementsEffects>(AchievementsState()) {

    override fun handleIntent(intent: AchievementsIntent) {
        when(intent) {
            AchievementsIntent.ShowListOfAchievements -> showListOfAchievements()
        }
    }

    private fun showListOfAchievements() {
        launch {
            try {
                val currentGymNamesAndCategory = noteInteractor.getGymNamesFromNotes()
                val maxNotes = mutableListOf<Note>()

                val currentNotes = noteInteractor.getAllNotes().first().toMutableList()
                if (currentNotes.isNotEmpty()) {
                    for (item in currentGymNamesAndCategory) {
                        val filteredNotes = currentNotes.filter { note ->
                            note.gymnasticName == item.gymnasticName && note.category == item.category
                        }
                        val sortedNotes = filteredNotes.sortedByDescending { it.heft }.sortedByDescending { it.reps }.first()
                        maxNotes.add(sortedNotes)
                    }
                    updateState {
                        copy(listOfAchievements = maxNotes)
                    }
                } else {
                    updateState {
                        copy(listOfAchievements = emptyList())
                    }
                }

            }catch (e: Exception) {
                e.printStackTrace()
                sendEffect(AchievementsEffects.ShowToast(AchievementsError.LoadFailed))
            }
        }
    }
}
