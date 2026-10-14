package com.example.gymnotes.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    val monthList = listOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )
    val dayMap = mapOf(
        "MONDAY" to "пн",
        "TUESDAY" to "вт",
        "WEDNESDAY" to "ср",
        "THURSDAY" to "чт",
        "FRIDAY" to "пт",
        "SATURDAY" to "сб",
        "SUNDAY" to "вс"
    )
    fun getMonthYear(localDate: LocalDate): String {
        val month = monthList[(localDate.monthValue - 1)]
        return "$month ${localDate.year}"
    }
    fun getDay(localDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd", Locale.getDefault())
        return localDate.format(formatter)
    }
    fun getDayOfWeek(localDate: LocalDate): String {
        return dayMap.getValue(localDate.dayOfWeek.toString())
    }
    fun parseIsoStringToLocalDate(isoString: String): LocalDate {
        return LocalDate.parse(isoString, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}