package com.example.shared.helper

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun formatDate(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val month = dateTime.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        val day = dateTime.dayOfMonth.toString().padStart(2, '0')
        val year = dateTime.year

        "$month $day, $year"
    } catch (e: Exception) {
        dateString
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun currentDateDisplay(): String {
    val now = Clock.System.now()
    val localDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val currentDate = localDate.format(LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth()
        chars(", ")
        year()
    })

    return currentDate
}