package com.example.gymnotes.presentation.notes

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.NoteItem
import com.example.gymnotes.presentation.BaseViewModel
import kotlinx.coroutines.Job

data class NotesState(
    val notesItems: List<NoteItem> = emptyList(),
    val error: String? = null
)

sealed class NotesIntent {
    data class LoadNotes(val addedAt: String) : NotesIntent()
}

sealed class NotesEffect {
    data class ShowToast(val error: NotesError) : NotesEffect()
}
sealed class NotesError {
    object LoadFailed : NotesError()
}

class NotesViewModel(
    private val noteInteractor: NoteInteractor
) : BaseViewModel<NotesState, NotesIntent, NotesEffect>(NotesState()) {

    private var loadNotesJob: Job? = null

    override fun handleIntent(intent: NotesIntent) {
        if(intent is NotesIntent.LoadNotes) {
            loadNotes(intent.addedAt)
        }
    }

    private fun loadNotes(addedAt: String) {
        loadNotesJob?.cancel()
        loadNotesJob = launch {
            try {
                noteInteractor.getNotesByDate(addedAt).collect { notes ->
                    val notesItemCurrent = mutableListOf<NoteItem>()
                    val notesItemCurrentGrouped = mutableListOf<NoteItem>()

                    // Сделал общий список из заголовков и данных
                    for (item in notes) {
                        if (!notesItemCurrent.contains(NoteItem.HeaderNoteItem(item.gymnasticName, item.category))) {
                            notesItemCurrent.add(NoteItem.HeaderNoteItem(item.gymnasticName, item.category))
                        }
                        notesItemCurrent.add(NoteItem.ContentNoteItem(item.gymnasticName, item.category, item.sets, item.reps, item.heft))
                    }

                    // Получаю список заголовков
                    val headerList = notesItemCurrent.filterIsInstance<NoteItem.HeaderNoteItem>()

                    // Получаю список данных
                    val contentList = notesItemCurrent.filterIsInstance<NoteItem.ContentNoteItem>()

                    // Наконец группирую, как надо
                    for (header in headerList) {
                        notesItemCurrentGrouped.add(header)
                        for (content in contentList) {
                            if (content.category == header.category && content.gymName == header.gymName) {
                                notesItemCurrentGrouped.add(content)
                            }
                        }
                    }

                    updateState {
                        copy(notesItems = notesItemCurrentGrouped)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(NotesEffect.ShowToast(NotesError.LoadFailed))
            }
        }
    }
}