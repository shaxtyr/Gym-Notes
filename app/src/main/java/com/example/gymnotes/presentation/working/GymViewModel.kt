package com.example.gymnotes.presentation.working

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.example.gymnotes.domain.interactor.GymnasticInteractor
import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.model.TimerMode
import com.example.gymnotes.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class GymState(
    val currentGymnastic: Gymnastic? = null,
    val timerMode: TimerMode = TimerMode.Stop,
    val currentTime: String = "00:00",
    val categoryForWatching: String = "Руки",
    val categoryForCreating: String = "",
    val gymItems: List<Gymnastic> = emptyList(),
    val isTimerActive: Boolean = false,
    val isFabPressed: Boolean = false,
    val isAddGymDialogVisible: Boolean = false,
    val isAddGymDetailsDialogVisible: Boolean = false,
    val isRenameGymDialogVisible: Boolean = false,
    val isRemoveGymDialogVisible: Boolean = false,
    val validation: GymInfo = GymInfo.Success,
    val newGymName: String = ""
)

sealed class GymIntent {
    data class AddNote(val reps: String, val heft: String, val gym: Gymnastic) : GymIntent()
    data class LoadGymsByCategory(val category: String) : GymIntent()
    data class AddGym(val category: String, val gymnastic: String) : GymIntent()
    data class RenameGym(val id: String, val newName: String) : GymIntent()
    data class RemoveGym(val id: String) : GymIntent()
    data class SelectGym(val gymnastic: Gymnastic) : GymIntent()
    data class SetNewName(val newGymName: String) : GymIntent()
    data class ChooseWatchingCategory(val category: String) : GymIntent()
    data class ShowAddGymDialogForCategory(val category: String) : GymIntent()
    data object ToggleFabMenu : GymIntent()
    data object ToggleTimerActive : GymIntent()
    data object HideAddGymDialog : GymIntent()
    data object ShowAddGymDetailsDialog : GymIntent()
    data object HideAddGymDetailsDialog : GymIntent()
    data object ShowRenameGymDialog : GymIntent()
    data object HideRenameGymDialog : GymIntent()
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
    data object RenameError : GymInfo()
    data object SuccessAddGym : GymInfo()
    data object SuccessRenameGym : GymInfo()
    data object SuccessRemoveGym : GymInfo()
    data object SuccessAddNote : GymInfo()
    data object Success : GymInfo()
}

class GymViewModel(
    private val workingInteractor: GymnasticInteractor,
    private val noteInteractor: NoteInteractor
) : BaseViewModel<GymState, GymIntent, GymEffects>(GymState()) {

    private var timerJob: Job? = null
    val filteredGymItems: List<Gymnastic>
        get() = currentState.gymItems.filter { it.category == currentState.categoryForWatching }

    init {
        loadGyms()
    }

    override fun handleIntent(intent: GymIntent) {
        when(intent) {
            is GymIntent.AddNote -> addNote(intent.reps, intent.heft, intent.gym)
            is GymIntent.LoadGymsByCategory -> loadGyms()
            is GymIntent.AddGym -> addGym(intent.category, intent.gymnastic)
            is GymIntent.RenameGym -> renameGym(intent.id, intent.newName)
            is GymIntent.RemoveGym -> removeGym(intent.id)
            is GymIntent.SetNewName -> setNewName(intent.newGymName)
            is GymIntent.ChooseWatchingCategory -> chooseWatchingCategory(intent.category)
            is GymIntent.SelectGym -> onGymSelected( intent.gymnastic)
            is GymIntent.ShowAddGymDialogForCategory -> showAddGymDialogForCategory(intent.category)
            GymIntent.ToggleFabMenu -> toggleFabMenu()
            GymIntent.ToggleTimerActive -> toggleTimerActive()
            GymIntent.HideAddGymDialog -> hideAddGymDialog()
            GymIntent.ShowAddGymDetailsDialog -> showAddGymDetailsDialog()
            GymIntent.HideAddGymDetailsDialog -> hideAddGymDetailsDialog()
            GymIntent.ShowRenameGymDialog -> showRenameGymDialog()
            GymIntent.HideRenameGymDialog -> hideRenameGymDialog()
            GymIntent.ShowRemoveGymDialog -> showRemoveGymDialog()
            GymIntent.HideRemoveGymDialog -> hideRemoveGymDialog()
            GymIntent.DefaultValidation -> defaultValidation()
        }
    }

    private fun renameGym(id: String, newName: String) {
        launch {
            try {
                workingInteractor.renameGymnastic(id, newName)
                updateState { copy(newGymName = "") }
                sendEffect(GymEffects.ShowInfo(GymInfo.SuccessRenameGym))
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowInfo(GymInfo.RenameError))
            }
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
                        //currentGymnastic = null
                    )
                }
                sendEffect(GymEffects.ShowInfo(GymInfo.SuccessRemoveGym))
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowInfo(GymInfo.RemoveError))
            }
        }
    }

    private fun loadGyms() {
        launch {
            try {
                workingInteractor.getAllGymnastics().collect{ gymnastics ->
                    updateState { copy(gymItems = gymnastics) }
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
                    isAddGymDetailsDialogVisible = false,
                    validation = GymInfo.Success
                ) }
                sendEffect(GymEffects.ShowInfo(GymInfo.SuccessAddNote))
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowInfo(GymInfo.AddNoteError))
            }
        }
    }
    private fun addGym(category: String, gymnasticName: String) {
        launch {
            if (gymnasticName.isBlank()) {
                updateState { copy(validation = GymInfo.GymNameEmpty) }
                return@launch
            }
            if (gymnasticName.length > GYM_NAME_MAX) {
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
                        validation = GymInfo.Success
                    )
                }
                sendEffect(GymEffects.ShowInfo(GymInfo.SuccessAddGym))
            } catch (e: Exception) {
                e.printStackTrace()
                sendEffect(GymEffects.ShowInfo(GymInfo.AddGymError))
            }
        }
    }
    private fun onGymSelected(gymnastic: Gymnastic) {
        updateState { copy(currentGymnastic = gymnastic) }
    }
    private fun toggleFabMenu() {
        updateState { copy(isFabPressed = !isFabPressed) }
    }

    private fun setNewName(newGymName: String) {
        updateState { copy(newGymName = newGymName) }
    }

    private fun chooseWatchingCategory(category: String) {
        updateState { copy(categoryForWatching = category) }
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

    private fun showRenameGymDialog() {
        updateState { copy(isRenameGymDialogVisible = true) }
    }

    private fun hideRenameGymDialog() {
        updateState { copy(isRenameGymDialogVisible = false) }
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

    private fun toggleTimerActive() {
        updateState { copy(isTimerActive = !state.value.isTimerActive) }
    }

    fun toggleTimer() {
        when(currentState.timerMode) {
            is TimerMode.Work -> pauseTimer()
            else -> startTimer()
        }
    }
     private fun startTimer() {
         timerJob?.cancel()
         timerJob = null
         updateState { copy(timerMode = TimerMode.Work) }
         timerJob = viewModelScope.launch {
             var currentSeconds = parseTimeToSeconds(currentState.currentTime)
             while (currentState.timerMode is TimerMode.Work) {
                 delay(1000.milliseconds) // Ждем 1 секунду
                 currentSeconds++
                 val formatted = formatSecondsToTime(currentSeconds)
                 updateState { copy(currentTime = formatted) }
             }
        }
    }
    private fun pauseTimer() {
        updateState { copy(timerMode = TimerMode.Pause) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        timerJob = null
        updateState { copy(
            timerMode = TimerMode.Stop,
            currentTime = "00:00"
        ) }
    }

    private fun parseTimeToSeconds(timeString: String): Int {
        val parts = timeString.split(":")
        if (parts.size == 2) {
            val minutes = parts[0].toIntOrNull() ?: 0
            val seconds = parts[1].toIntOrNull() ?: 0
            return minutes * 60 + seconds
        }
        return 0
    }

    @SuppressLint("DefaultLocale")
    private fun formatSecondsToTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    companion object {
        private const val REPS_MAX = 10_000
        private const val HEFT_MAX = 100_000
        private const val GYM_NAME_MAX = 100
    }
}

