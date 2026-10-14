package com.example.gymnotes.presentation.working

import android.util.Log
import com.example.gymnotes.domain.interactor.GymnasticInteractor
import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.presentation.BaseViewModel
import kotlinx.coroutines.flow.first

data class GymnasticsState(
    val currentGymnastic: Gymnastic,
    val gymnasticItems: List<Gymnastic> = emptyList(),
    val isAddDialogVisible: Boolean = false,
    val isAddDetailsDialogVisible: Boolean = false,
    val isRenameDialogVisible: Boolean = false,
    val isRemoveDialogVisible: Boolean = false,
    val isGymnasticSelectedVisible: Boolean = false
)
sealed class GymnasticsIntent {
    data class AddNote(val reps: String, val heft: String, val gymnastic: Gymnastic) : GymnasticsIntent()
    data class LoadGymnasticsByCategory(val category: String) : GymnasticsIntent()
    data class AddGymnastic(val category: String, val gymnastic: String) : GymnasticsIntent()
    data class RemoveGymnastic(val id: String) : GymnasticsIntent()
    data class SelectGymnastic(val gymnastic: Gymnastic) : GymnasticsIntent()
    data object ShowAddGymnasticDialog : GymnasticsIntent()
    data object HideAddGymnasticDialog : GymnasticsIntent()
    data object ShowAddDetailsGymnasticDialog : GymnasticsIntent()
    data object HideAddDetailsGymnasticDialog : GymnasticsIntent()
    data object ShowRemoveGymnasticDialog : GymnasticsIntent()
    data object HideRemoveGymnasticDialog : GymnasticsIntent()
}

sealed class GymnasticsEffects {
    data class ShowToast(val error: GymnasticError) : GymnasticsEffects()
}

sealed class GymnasticError {
    object LoadFailed : GymnasticError()
    object AddFailed : GymnasticError()
    object RemoveFailed : GymnasticError()
}

class GymnasticsViewModel(
    private val workingInteractor: GymnasticInteractor,
    private val noteInteractor: NoteInteractor
) : BaseViewModel<GymnasticsState, GymnasticsIntent, GymnasticsEffects>(GymnasticsState(
    currentGymnastic = Gymnastic(
        id = "",
        name = "",
        category = ""
    )
)) {

    override fun handleIntent(intent: GymnasticsIntent) {
        when(intent) {
            is GymnasticsIntent.AddNote -> addNote(intent.reps, intent.heft, intent.gymnastic)
            is GymnasticsIntent.LoadGymnasticsByCategory -> loadGymnastics(intent.category)
            is GymnasticsIntent.AddGymnastic -> addGymnastic(intent.category, intent.gymnastic)
            is GymnasticsIntent.RemoveGymnastic -> removeGymnastic(intent.id)
            is GymnasticsIntent.SelectGymnastic -> onGymnasticSelected( intent.gymnastic)
            GymnasticsIntent.ShowAddGymnasticDialog -> showAddGymnasticDialog()
            GymnasticsIntent.HideAddGymnasticDialog -> hideAddGymnasticDialog()
            GymnasticsIntent.ShowAddDetailsGymnasticDialog -> showAddDetailsGymnasticDialog()
            GymnasticsIntent.HideAddDetailsGymnasticDialog -> hideAddDetailsGymnasticDialog()
            GymnasticsIntent.ShowRemoveGymnasticDialog -> showRemoveGymnasticDialog()
            GymnasticsIntent.HideRemoveGymnasticDialog -> hideRemoveGymnasticDialog()
        }
    }
    private fun removeGymnastic(id: String) {
        launch {
            try {
                workingInteractor.removeGymnastic(id)
                val newGymnastics = currentState.gymnasticItems.filter { item ->
                    item.id != id
                }
                updateState {
                    copy(
                        gymnasticItems = newGymnastics,
                        isGymnasticSelectedVisible = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymnasticsEffects.ShowToast(GymnasticError.RemoveFailed))
            }
        }
    }
    private fun loadGymnastics(category: String) {
        launch {
            try {
                workingInteractor.getGymnasticsByCategory(category).collect { gymnasticsByCategory ->
                    updateState {
                        copy(gymnasticItems = gymnasticsByCategory)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymnasticsEffects.ShowToast(GymnasticError.LoadFailed))
            }
        }
    }
    private fun addNote(reps: String, heft: String, gymnastic: Gymnastic) {
        launch {
            try {
                noteInteractor.addNote(reps, heft, gymnastic)
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymnasticsEffects.ShowToast(GymnasticError.AddFailed))
            }
        }
    }
    private fun addGymnastic(category: String, gymnasticName: String) {
        launch {
            try {
                workingInteractor.addGymnastic(category, gymnasticName)
                val currentGymnastics = workingInteractor.getGymnasticsByCategory(category).first().toMutableList()
                updateState {
                    copy(
                        gymnasticItems = currentGymnastics,
                        isAddDialogVisible = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymnasticsEffects.ShowToast(GymnasticError.AddFailed))
            }
        }
    }

    private fun onGymnasticSelected(gymnastic: Gymnastic) {
        updateState { copy(
            currentGymnastic = gymnastic,
            isGymnasticSelectedVisible = true
        ) }
    }

    private fun showAddGymnasticDialog() {
        updateState { copy(isAddDialogVisible = true) }
    }

    private fun hideAddGymnasticDialog() {
        updateState { copy(isAddDialogVisible = false) }
    }

    private fun showAddDetailsGymnasticDialog() {
        updateState { copy(isAddDetailsDialogVisible = true) }
    }

    private fun hideAddDetailsGymnasticDialog() {
        updateState { copy(isAddDetailsDialogVisible = false) }
    }

    private fun showRemoveGymnasticDialog() {
        updateState { copy(isRemoveDialogVisible = true) }
    }

    private fun hideRemoveGymnasticDialog() {
        updateState { copy(isRemoveDialogVisible = false) }
    }
}

