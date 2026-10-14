package com.example.gymnotes.presentation.notes

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.presentation.BaseViewModel

data class NotesState(
    val notesItems: List<Note> = emptyList(),
    val error: String? = null
)

sealed class NotesIntent {
    data class LoadNotes(val addedAt: String) : NotesIntent()
    data class RemoveNotes(val id: String) : NotesIntent()
}

sealed class NotesEffect {
    data class ShowToast(val error: NotesError) : NotesEffect()
}
sealed class NotesError {
    object LoadFailed : NotesError()
    object AddFailed : NotesError()
    object RemoveFailed : NotesError()
    object EditFailed : NotesError()
}

class NotesViewModel(
    private val noteInteractor: NoteInteractor
) : BaseViewModel<NotesState, NotesIntent, NotesEffect>(NotesState()) {

    override fun handleIntent(intent: NotesIntent) {
        when(intent) {
            is NotesIntent.LoadNotes -> loadNotes(intent.addedAt)
            is NotesIntent.RemoveNotes -> removeNotes(intent.id)
        }
    }

    private fun loadNotes(addedAt: String) {
        launch {
            try {
                noteInteractor.getNotesByDate(addedAt).collect { notes ->
                    updateState {
                        copy(notesItems = notes)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(NotesEffect.ShowToast(NotesError.LoadFailed))
            }
        }
    }

    private fun removeNotes(id: String) {
        launch {
            try {
                noteInteractor.removeNote(id)
                val newNotesList = currentState.notesItems.filter { item ->
                    item.id != id
                }
                updateState {
                    copy(notesItems = newNotesList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(NotesEffect.ShowToast(NotesError.RemoveFailed))
            }
        }
    }

}