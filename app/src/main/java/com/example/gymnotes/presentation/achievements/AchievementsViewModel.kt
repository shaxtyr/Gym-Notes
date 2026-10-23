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
                val currentGymNamesFromNotes = noteInteractor.getGymNamesFromNotes()
                val maxNotes = mutableListOf<Note>()

                val currentListOfAchievements = noteInteractor.getAllNotes().first().toMutableList()
                if (currentListOfAchievements.isNotEmpty()) {
                    var maxNote = currentListOfAchievements.first()

                    // Сравниваю по названию упражнения, по макс. весу и затем по кол-ву повторов
                    for (gymName in currentGymNamesFromNotes) {
                        for (note in currentListOfAchievements) {
                            if (note.gymnasticName == gymName && note.heft.toInt() > maxNote.heft.toInt()) {
                                maxNote = note
                            } else if (note.gymnasticName == gymName && note.heft.toInt() == maxNote.heft.toInt() && note.reps.toInt() > maxNote.reps.toInt()) {
                                maxNote = note
                            }
                        }
                        maxNotes.add(maxNote)
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
