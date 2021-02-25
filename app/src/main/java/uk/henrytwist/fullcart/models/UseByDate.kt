package uk.henrytwist.fullcart.models

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

data class UseByDate(val date: LocalDate) {

    fun toEpochDay() = date.toEpochDay()

    fun toEpochMillis() = date.atStartOfDay(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toInstant().toEpochMilli()

    fun toFullString(): String = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(date)

    fun toShortString(): String = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(date)

    fun isToUseSoon(now: LocalDate, threshold: Int): Boolean {

        return now.until(date, ChronoUnit.DAYS) <= threshold
    }

    companion object {

        fun fromEpochDay(epochDay: Long) = UseByDate(LocalDate.ofEpochDay(epochDay))

        fun fromEpochMillis(epochMillis: Long) = UseByDate(Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate())
    }
}