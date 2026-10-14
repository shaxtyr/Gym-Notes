package com.example.gymnotes.presentation.notes

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.NotesDateGrid
import com.example.gymnotes.presentation.BaseViewModel

data class NotesDateState(
    val gridNotesDateItems: List<NotesDateGrid> = emptyList(),
    val error: String? = null
)

sealed class NotesDateIntent {
    data object LoadNotesDate : NotesDateIntent()
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
    private val noteInteractor: NoteInteractor
) : BaseViewModel<NotesDateState, NotesDateIntent, NotesDateEffect>(NotesDateState()) {

    init {
        loadDatesFromNotes()
    }

    private fun loadDatesFromNotes() {
        launch {
            try {
                noteInteractor.getAllNotes().collect { notes ->
                    val gridNoteItemsCurrent = mutableListOf<NotesDateGrid>()

                    //создаю новый комбинированный список
                    for (item in notes) {
                        if (!gridNoteItemsCurrent.contains(NotesDateGrid.HeaderNotesDate(item.monthYear))) {
                            gridNoteItemsCurrent.add(NotesDateGrid.HeaderNotesDate(item.monthYear))
                        }
                        if (!gridNoteItemsCurrent.contains(NotesDateGrid.ContentNotesDate(item.day, item.dayOfWeek, item.addedAt))) {
                            gridNoteItemsCurrent.add(NotesDateGrid.ContentNotesDate(item.day, item.dayOfWeek, item.addedAt))
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

    override fun handleIntent(intent: NotesDateIntent) {
        when(intent) {
            NotesDateIntent.LoadNotesDate -> loadDatesFromNotes()
            is NotesDateIntent.RemoveNotesDate -> removeNotesDate(intent.date)
            is NotesDateIntent.EditNotesDate -> editNotesDate(intent.id)
            is NotesDateIntent. ShowNotes -> showNotes(intent.addedAt)
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

    private fun editNotesDate(id: String) {

    }

    private fun showNotes(addedAt: String) {
        sendEffect(NotesDateEffect.NavigateToNotes(addedAt))
    }

}