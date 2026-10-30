package com.example.gymnotes.domain.model

sealed class TimerMode {
    object Stop : TimerMode()
    object Work : TimerMode()
    object Pause : TimerMode()
}