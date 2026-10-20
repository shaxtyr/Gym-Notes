package com.example.gymnotes.presentation.notes

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.interactor.ThemeInteractor
import com.example.gymnotes.domain.model.NotesDateGridItem
import com.example.gymnotes.presentation.BaseViewModel

data class NotesDateState(
    val gridNotesDateItems: List<NotesDateGridItem> = emptyList(),
    val isDarkTheme: Boolean = false,
    val isShowingHelp: Boolean = false,
    val error: String? = null
)

sealed class NotesDateIntent {
    data object LoadNotesDate : NotesDateIntent()
    data object ToggleTheme : NotesDateIntent()
    data object ShowHelp : NotesDateIntent()
    data class RemoveNotesDate(val date: String) : NotesDateIntent()
    data class EditNotesDate(val id: String) : NotesDateIntent()
    data class ShowNotes(val addedAt: String) : NotesDateIntent()
}

sealed class NotesDateEffect {
    data class NavigateToNotes(val addedAt: String) : NotesDateEffect()
    data class ShowToast(val error: NotesDateError) : NotesDateEffect()
}

sealed class NotesDateError {
    object LoadFailed : NotesDateError()
    object AddFailed : NotesDateError()
    object RemoveFailed : NotesDateError()
    object EditFailed : NotesDateError()
}

class NotesDateViewModel(
    private val noteInteractor: NoteInteractor,
    private val themeInteractor: ThemeInteractor
) : BaseViewModel<NotesDateState, NotesDateIntent, NotesDateEffect>(NotesDateState()) {

    init {
        loadDatesFromNotes()
        loadTheme()
    }

    override fun handleIntent(intent: NotesDateIntent) {
        when(intent) {
            NotesDateIntent.LoadNotesDate -> loadDatesFromNotes()
            NotesDateIntent.ToggleTheme -> toggleTheme()
            NotesDateIntent.ShowHelp -> showHelp()
            is NotesDateIntent.RemoveNotesDate -> removeNotesDate(intent.date)
            is NotesDateIntent.EditNotesDate -> editNotesDate(intent.id)
            is NotesDateIntent. ShowNotes -> showNotes(intent.addedAt)
        }
    }

    private fun loadDatesFromNotes() {
        launch {
            try {
                noteInteractor.getAllNotes().collect { notes ->
                    val gridNoteItemsCurrent = mutableListOf<NotesDateGridItem>()

                    //создаю новый комбинированный список
                    for (item in notes) {
                        if (!gridNoteItemsCurrent.contains(NotesDateGridItem.HeaderNotesDateItem(item.monthYear))) {
                            gridNoteItemsCurrent.add(NotesDateGridItem.HeaderNotesDateItem(item.monthYear))
                        }
                        if (!gridNoteItemsCurrent.contains(NotesDateGridItem.ContentNotesDateItem(item.day, item.dayOfWeek, item.addedAt))) {
                            gridNoteItemsCurrent.add(NotesDateGridItem.ContentNotesDateItem(item.day, item.dayOfWeek, item.addedAt))
                        }
                    }
                    updateState {
                        copy(
                            gridNotesDateItems = gridNoteItemsCurrent
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(NotesDateEffect.ShowToast(NotesDateError.LoadFailed))
            }
        }
    }
    private fun removeNotesDate(date: String) {
        launch {
            try {
                noteInteractor.removeNotesByDate(date)
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(NotesDateEffect.ShowToast(NotesDateError.RemoveFailed))
            }
        }
    }

    private fun loadTheme() {
        launch {
            themeInteractor.getIsDarkTheme().collect { isDark ->
                updateState { copy(isDarkTheme = isDark) }
            }
        }
    }
    private fun toggleTheme() {
        val newTheme = !state.value.isDarkTheme
        launch {
            themeInteractor.setDarkTheme(newTheme)
        }
        updateState { copy(isDarkTheme = newTheme) }
    }

    private fun showHelp() {
    }

    private fun editNotesDate(id: String) {

    }

    private fun showNotes(addedAt: String) {
        sendEffect(NotesDateEffect.NavigateToNotes(addedAt))
    }

}