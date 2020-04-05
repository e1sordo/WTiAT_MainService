package es.e1sordo.thesis.wtiat.corewebserviceapi.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

val times: Array<Long> = arrayOf(
    TimeUnit.DAYS.toMillis(365),
    TimeUnit.DAYS.toMillis(30),
    TimeUnit.DAYS.toMillis(1),
    TimeUnit.HOURS.toMillis(1),
    TimeUnit.MINUTES.toMillis(1),
    TimeUnit.SECONDS.toMillis(1)
)

private val timesString: Array<String> = arrayOf(
    "year", "month", "day", "hour", "minute", "second"
)

fun LocalDateTime.howLongAgoItWasBeauty(): String? {
    val duration = howLongAgoItWasInMillis()

    val res = StringBuffer()
    for (i in times.indices) {
        val current: Long = times[i]
        val temp = duration / current
        if (temp > 0) {
            res.append(temp).append(" ").append(timesString[i]).append(if (temp != 1L) "s" else "")
                .append(" ago")
            break
        }
    }
    return if ("" == res.toString()) "Just now" else res.toString()
}

fun LocalDateTime.howLongAgoItWasInMillis() = Duration.between(this, getCurrentTime()).toMillis()

fun getCurrentTime(): LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"))
