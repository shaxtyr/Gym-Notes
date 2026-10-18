package com.example.gymnotes.presentation.working

import android.util.Log
import com.example.gymnotes.domain.interactor.GymnasticInteractor
import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.presentation.BaseViewModel
import kotlinx.coroutines.flow.first

data class GymState(
    val currentGymnastic: Gymnastic,
    val categoryForWatching: String = "Руки",
    val categoryForCreating: String = "",
    val gymItems: List<Gymnastic> = emptyList(),
    val isAddGymDialogVisible: Boolean = false,
    val isAddGymDetailsDialogVisible: Boolean = false,
    val isRenameGymDialogVisible: Boolean = false,
    val isRemoveGymDialogVisible: Boolean = false,
    val isGymSelectedVisible: Boolean = false
)

sealed class GymIntent {
    data class AddNote(val reps: String, val heft: String, val gym: Gymnastic) : GymIntent()
    data class LoadGymsByCategory(val category: String) : GymIntent()
    data class AddGym(val category: String, val gymnastic: String) : GymIntent()
    data class RemoveGym(val id: String) : GymIntent()
    data class SelectGym(val gymnastic: Gymnastic) : GymIntent()
    data class ShowAddGymDialogForCategory(val category: String) : GymIntent()
    data object HideAddGymDialog : GymIntent()
    data object ShowAddGymDetailsDialog : GymIntent()
    data object HideAddGymDetailsDialog : GymIntent()
    data object ShowRemoveGymDialog : GymIntent()
    data object HideRemoveGymDialog : GymIntent()
}

sealed class GymEffects {
    data class ShowToast(val error: GymError) : GymEffects()
}

sealed class GymError {
    object LoadFailed : GymError()
    object AddFailed : GymError()
    object RemoveFailed : GymError()
}

class GymViewModel(
    private val workingInteractor: GymnasticInteractor,
    private val noteInteractor: NoteInteractor
) : BaseViewModel<GymState, GymIntent, GymEffects>(GymState(
    currentGymnastic = Gymnastic(
        id = "",
        name = "",
        category = ""
    )
)) {

    override fun handleIntent(intent: GymIntent) {
        when(intent) {
            is GymIntent.AddNote -> addNote(intent.reps, intent.heft, intent.gym)
            is GymIntent.LoadGymsByCategory -> loadGyms(intent.category)
            is GymIntent.AddGym -> addGym(intent.category, intent.gymnastic)
            is GymIntent.RemoveGym -> removeGym(intent.id)
            is GymIntent.SelectGym -> onGymSelected( intent.gymnastic)
            is GymIntent.ShowAddGymDialogForCategory -> showAddGymDialogForCategory(intent.category)
            GymIntent.HideAddGymDialog -> hideAddGymDialog()
            GymIntent.ShowAddGymDetailsDialog -> showAddGymDetailsDialog()
            GymIntent.HideAddGymDetailsDialog -> hideAddGymDetailsDialog()
            GymIntent.ShowRemoveGymDialog -> showRemoveGymDialog()
            GymIntent.HideRemoveGymDialog -> hideRemoveGymDialog()
        }
    }
    private fun removeGym(id: String) {
        launch {
            try {
                workingInteractor.removeGymnastic(id)
                val newGymnastics = currentState.gymItems.filter { item ->
                    item.id != id
                }
                updateState {
                    copy(
                        gymItems = newGymnastics,
                        isGymSelectedVisible = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowToast(GymError.RemoveFailed))
            }
        }
    }
    private fun loadGyms(category: String) {
        launch {
            try {
                workingInteractor.getGymnasticsByCategory(category).collect { gymnasticsByCategory ->
                    updateState {
                        copy(
                            gymItems = gymnasticsByCategory,
                            categoryForWatching = category
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowToast(GymError.LoadFailed))
            }
        }
    }
    private fun addNote(reps: String, heft: String, gymnastic: Gymnastic) {
        launch {
            try {
                noteInteractor.addNote(reps, heft, gymnastic)
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowToast(GymError.AddFailed))
            }
        }
    }
    private fun addGym(category: String, gymnasticName: String) {
        launch {
            try {
                workingInteractor.addGymnastic(category, gymnasticName)
                val currentGymnastics = workingInteractor.getGymnasticsByCategory(category).first().toMutableList()
                updateState {
                    copy(
                        categoryForWatching = category,
                        gymItems = currentGymnastics,
                        isAddGymDialogVisible = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowToast(GymError.AddFailed))
            }
        }
    }

    private fun onGymSelected(gymnastic: Gymnastic) {
        updateState { copy(
            currentGymnastic = gymnastic,
            isGymSelectedVisible = true
        ) }
    }

    private fun showAddGymDialogForCategory(category: String) {
        updateState {
            copy(isAddGymDialogVisible = true, categoryForCreating = category) }
    }

    private fun hideAddGymDialog() {
        updateState { copy(isAddGymDialogVisible = false) }
    }

    private fun showAddGymDetailsDialog() {
        updateState { copy(isAddGymDetailsDialogVisible = true) }
    }

    private fun hideAddGymDetailsDialog() {
        updateState { copy(isAddGymDetailsDialogVisible = false) }
    }

    private fun showRemoveGymDialog() {
        updateState { copy(isRemoveGymDialogVisible = true) }
    }

    private fun hideRemoveGymDialog() {
        updateState { copy(isRemoveGymDialogVisible = false) }
    }
}
