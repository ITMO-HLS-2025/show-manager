package ru.itmo.hls.showmanager.application.dto

import java.time.LocalDateTime

data class ShowDto(
    val id: Long,
    val title: String,
    val description: String,
    val date: LocalDateTime,
    val durationMinutes: Int?,
    val hall: HallViewDto,
    val theatre: TheatreViewDto
)

data class ShowCreateDto(
    val date: LocalDateTime,
    val performanceId: Long,
    val hallId: Long
)

data class ShowViewDto(
    val id: Long,
    val title: String,
    val date: LocalDateTime
)
