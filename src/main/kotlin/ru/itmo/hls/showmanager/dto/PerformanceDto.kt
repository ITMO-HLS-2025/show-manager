package ru.itmo.hls.showmanager.dto

data class PerformanceDto(
    val id: Long,
    val title: String,
    val description: String?,
    val durationMinutes: Int?,
    val theatreIds: List<Long>
)

data class PerformanceCreateDto(
    val title: String,
    val description: String?,
    val durationMinutes: Int?,
    val theatreIds: List<Long>
)
