package com.example.gymnotes.presentation.notes

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.interactor.ThemeInteractor
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.domain.model.NotesDateGridItem
import com.example.gymnotes.presentation.BaseViewModel

data class NotesDateState(
    val gridNotesDateItems: List<NotesDateGridItem> = emptyList(),
    val currentGridNotesDateItems: NotesDateGridItem.ContentNotesDateItem,
    val isDarkTheme: Boolean = false,
    val isShowingHelp: Boolean = false,
    val isDateSelectedVisible: Boolean = false,
    val isRemoveDateDialogVisible: Boolean = false,
    val dateToRemove: String = "",
    val error: String? = null
)

sealed class NotesDateIntent {
    data object LoadNotesDate : NotesDateIntent()
    data object ToggleTheme : NotesDateIntent()
    data object ShowHelp : NotesDateIntent()
    data class SelectDate(val addedAt: String) : NotesDateIntent()
    data class RemoveNotesDate(val addedAt: String) : NotesDateIntent()
    data class EditNotesDate(val id: String) : NotesDateIntent()
    data class ShowNotes(val addedAt: String) : NotesDateIntent()
    data object ShowRemoveDateDialog : NotesDateIntent()
    data object HideRemoveDateDialog : NotesDateIntent()
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
) : BaseViewModel<NotesDateState, NotesDateIntent, NotesDateEffect>(NotesDateState(
    currentGridNotesDateItems = NotesDateGridItem.ContentNotesDateItem(
        day = "",
        dayOfWeek = "",
        addedAt = ""
    )
)) {

    init {
        loadDatesFromNotes()
        loadTheme()
    }

    override fun handleIntent(intent: NotesDateIntent) {
        when(intent) {
            NotesDateIntent.LoadNotesDate -> loadDatesFromNotes()
            NotesDateIntent.ToggleTheme -> toggleTheme()
            NotesDateIntent.ShowHelp -> showHelp()
            NotesDateIntent.ShowRemoveDateDialog -> showRemoveDateDialog()
            NotesDateIntent.HideRemoveDateDialog -> hideRemoveDateDialog()
            is NotesDateIntent.RemoveNotesDate -> removeNotesDate(intent.addedAt)
            is NotesDateIntent.EditNotesDate -> editNotesDate(intent.id)
            is NotesDateIntent. ShowNotes -> showNotes(intent.addedAt)
            is NotesDateIntent. SelectDate -> onNoteDateSelected(intent.addedAt)
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
                val newGridNotesDateItems = currentState.gridNotesDateItems.filter { item ->
                    when (item) {
                        is NotesDateGridItem.ContentNotesDateItem -> item.day != date
                        is NotesDateGridItem.HeaderNotesDateItem -> true // Заголовок оставляем
                    }
                }
                updateState { copy(
                    isDateSelectedVisible = false,
                    gridNotesDateItems = newGridNotesDateItems
                ) }
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

    private fun showRemoveDateDialog() {
        updateState { copy(isRemoveDateDialogVisible = true) }
    }
    private fun hideRemoveDateDialog() {
        updateState { copy(isRemoveDateDialogVisible = false) }
    }
    private fun onNoteDateSelected(addedAt: String) {
        updateState { copy(
            dateToRemove = addedAt,
            isDateSelectedVisible = true
        ) }
    }


}