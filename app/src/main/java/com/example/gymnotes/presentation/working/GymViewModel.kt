package com.example.gymnotes.presentation.working

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
    val isFabPressed: Boolean = false,
    val isAddGymDialogVisible: Boolean = false,
    val isAddGymDetailsDialogVisible: Boolean = false,
    val isRenameGymDialogVisible: Boolean = false,
    val isRemoveGymDialogVisible: Boolean = false,
    val isGymSelectedVisible: Boolean = false,
    val validation: GymInfo = GymInfo.Success
)

sealed class GymIntent {
    data class AddNote(val reps: String, val heft: String, val gym: Gymnastic) : GymIntent()
    data class LoadGymsByCategory(val category: String) : GymIntent()
    data class AddGym(val category: String, val gymnastic: String) : GymIntent()
    data class RemoveGym(val id: String) : GymIntent()
    data class SelectGym(val gymnastic: Gymnastic) : GymIntent()
    data class ShowAddGymDialogForCategory(val category: String) : GymIntent()
    data object ToggleFabMenu : GymIntent()
    data object HideAddGymDialog : GymIntent()
    data object ShowAddGymDetailsDialog : GymIntent()
    data object HideAddGymDetailsDialog : GymIntent()
    data object ShowRemoveGymDialog : GymIntent()
    data object HideRemoveGymDialog : GymIntent()
    data object DefaultValidation : GymIntent()
}

sealed class GymEffects {
    //data class ShowSuccess(val message: String) : GymEffects()
    data class ShowInfo(val info: GymInfo) : GymEffects()
}

sealed class GymInfo {
    data object GymNameEmpty : GymInfo()
    data object GymNameOver : GymInfo()
    data object HeftEmpty : GymInfo()
    data object HeftZero : GymInfo()
    data object HeftOver : GymInfo()
    data object RepsEmpty : GymInfo()
    data object RepsZero : GymInfo()
    data object RepsOver : GymInfo()
    data object LoadError : GymInfo()
    data object AddGymError : GymInfo()
    data object AddNoteError : GymInfo()
    data object RemoveError : GymInfo()
    data object SuccessGym : GymInfo()
    data object SuccessNote : GymInfo()
    data object Success : GymInfo()
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
            GymIntent.ToggleFabMenu -> toggleFabMenu()
            GymIntent.HideAddGymDialog -> hideAddGymDialog()
            GymIntent.ShowAddGymDetailsDialog -> showAddGymDetailsDialog()
            GymIntent.HideAddGymDetailsDialog -> hideAddGymDetailsDialog()
            GymIntent.ShowRemoveGymDialog -> showRemoveGymDialog()
            GymIntent.HideRemoveGymDialog -> hideRemoveGymDialog()
            GymIntent.DefaultValidation -> defaultValidation()
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
                sendEffect(GymEffects.ShowInfo(GymInfo.RemoveError))
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
                sendEffect(GymEffects.ShowInfo(GymInfo.LoadError))
            }
        }
    }
    private fun addNote(reps: String, heft: String, gymnastic: Gymnastic) {
        launch {
            if (reps.isBlank()) {
                updateState { copy(validation = GymInfo.RepsEmpty) }
                return@launch
            }
            if (reps.toInt() == 0) {
                updateState { copy(validation = GymInfo.RepsZero) }
                return@launch
            }
            if (reps.toInt() > REPS_MAX) {
                updateState { copy(validation = GymInfo.RepsOver) }
                return@launch
            }
            if (heft.isBlank()) {
                updateState { copy(validation = GymInfo.HeftEmpty) }
                return@launch
            }
            if (heft.toInt() == 0) {
                updateState { copy(validation = GymInfo.HeftZero) }
                return@launch
            }
            if (heft.toInt() > HEFT_MAX) {
                updateState { copy(validation = GymInfo.HeftOver) }
                return@launch
            }
            try {
                noteInteractor.addNote(reps, heft, gymnastic)
                updateState { copy(
                    isAddGymDetailsDialogVisible = false
                ) }
                sendEffect(GymEffects.ShowInfo(GymInfo.SuccessNote))
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowInfo(GymInfo.AddNoteError))
            }
        }
    }
    private fun addGym(category: String, gymnasticName: String) {
        launch {
            if (gymnasticName.isBlank()) {
                //sendEffect(GymEffects.ShowInfo(GymInfo.GymNameEmpty))
                updateState { copy(validation = GymInfo.GymNameEmpty) }
                return@launch
            }
            if (gymnasticName.length > GYM_NAME_MAX) {
                //sendEffect(GymEffects.ShowInfo(GymInfo.GymNameOver))
                updateState { copy(validation = GymInfo.GymNameOver) }
                return@launch
            }
            try {
                workingInteractor.addGymnastic(category, gymnasticName)
                val currentGymnastics = workingInteractor.getGymnasticsByCategory(category).first().toMutableList()
                updateState {
                    copy(
                        categoryForWatching = category,
                        gymItems = currentGymnastics,
                        isAddGymDialogVisible = false,
                    )
                }
                sendEffect(GymEffects.ShowInfo(GymInfo.SuccessGym))
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowInfo(GymInfo.AddGymError))
            }
        }
    }

    private fun onGymSelected(gymnastic: Gymnastic) {
        updateState { copy(
            currentGymnastic = gymnastic,
            isGymSelectedVisible = true
        ) }
    }

    private fun toggleFabMenu() {
        updateState { copy(isFabPressed = !isFabPressed) }
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

    private fun defaultValidation() {
        updateState { copy(validation = GymInfo.Success) }
    }

    companion object {
        private const val REPS_MAX = 10_000
        private const val HEFT_MAX = 100_000
        private const val GYM_NAME_MAX = 100
    }
}

