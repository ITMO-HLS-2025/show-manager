package ru.itmo.hls.showmanager.mapper

import ru.itmo.hls.showmanager.dto.PerformanceDto
import ru.itmo.hls.showmanager.entity.Performance

fun Performance.toDto(): PerformanceDto =
    PerformanceDto(
        id = id,
        title = title,
        description = description,
        durationMinutes = durationMinutes,
        theatreIds = theatreIds.toList()
    )
