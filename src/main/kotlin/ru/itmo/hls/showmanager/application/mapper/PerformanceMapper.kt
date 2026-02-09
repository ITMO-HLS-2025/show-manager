package ru.itmo.hls.showmanager.application.mapper

import ru.itmo.hls.showmanager.application.dto.PerformanceDto
import ru.itmo.hls.showmanager.domain.model.Performance

fun Performance.toDto(): PerformanceDto =
    PerformanceDto(
        id = id,
        title = title,
        description = description,
        durationMinutes = durationMinutes
    )
