package com.example.gymnotes.presentation.working

import com.example.gymnotes.domain.interactor.GymnasticInteractor
import com.example.gymnotes.presentation.BaseViewModel

data class StartWorkingState(
    val isLoading: Boolean = false,
    val error: String? = null
)
sealed class StartWorkingIntent {
    data class ChooseCategory(val category: String) : StartWorkingIntent()
}
sealed class StartWorkingEffect {
    data class NavigateToGymnasticsScreen(val category: String) : StartWorkingEffect()
}

class StartWorkingViewModel : BaseViewModel<StartWorkingState, StartWorkingIntent, StartWorkingEffect>(initialState = StartWorkingState()) {
    override fun handleIntent(intent: StartWorkingIntent) {
        when(intent) {
            is StartWorkingIntent.ChooseCategory -> sendEffect(
                StartWorkingEffect.NavigateToGymnasticsScreen(intent.category)
            )
        }
    }
}