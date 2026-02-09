package ru.itmo.hls.showmanager.application.mapper

import ru.itmo.hls.showmanager.application.dto.HallViewDto
import ru.itmo.hls.showmanager.application.dto.ShowDto
import ru.itmo.hls.showmanager.application.dto.ShowViewDto
import ru.itmo.hls.showmanager.application.dto.TheatreViewDto
import ru.itmo.hls.showmanager.domain.model.Show

fun Show.toDto(hall: HallViewDto, theatre: TheatreViewDto): ShowDto =
    ShowDto(
        id = id,
        title = performance.title,
        description = performance.description.orEmpty(),
        date = showTime,
        durationMinutes = performance.durationMinutes,
        hall = hall,
        theatre = theatre
    )

fun Show.toViewDto(): ShowViewDto =
    ShowViewDto(
        id = id,
        title = performance.title,
        date = showTime
    )
