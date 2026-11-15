package com.example.gymnotes.presentation.achievements

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.presentation.BaseViewModel

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

    val listOfAchievementsSorted: List<Note>
        get() = currentState.listOfAchievements
            .sortedWith(
                compareByDescending<Note> { it.heft }
                    .thenByDescending { it.reps }
            )
            .distinctBy { it.gymnasticName to it.category }

    init {
        showListOfAchievements()
    }

    override fun handleIntent(intent: AchievementsIntent) {
        when(intent) {
            AchievementsIntent.ShowListOfAchievements -> showListOfAchievements()
        }
    }

    private fun showListOfAchievements() {
        launch {
            try {
                noteInteractor.getAllNotes().collect{ notes ->
                    updateState { copy(listOfAchievements = notes) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(AchievementsEffects.ShowToast(AchievementsError.LoadFailed))
            }
        }
    }
}
