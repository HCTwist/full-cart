package uk.henrytwist.fullcart.data

import java.time.*

object TimeConverter {

    fun toLocalDateTime(epochSecond: Long): LocalDateTime {

        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault())
    }

    fun toEpochSeconds(localDateTime: LocalDateTime): Long {

        return localDateTime.toEpochSecond(ZoneOffset.UTC)
    }

    fun toEpochSeconds(localDateTime: LocalDateTime?): Long? {

        return if (localDateTime == null) null else toEpochSeconds(localDateTime)
    }
}