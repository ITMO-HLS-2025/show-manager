package ru.itmo.hls.showmanager.mapper

import ru.itmo.hls.showmanager.dto.HallViewDto
import ru.itmo.hls.showmanager.dto.ShowDto
import ru.itmo.hls.showmanager.dto.ShowViewDto
import ru.itmo.hls.showmanager.dto.TheatreViewDto
import ru.itmo.hls.showmanager.entity.Show

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
