package ru.itmo.hls.showmanager.application.port

import ru.itmo.hls.showmanager.application.dto.TheatreViewDto

interface TheatreClient {
    fun getTheatre(id: Long): TheatreViewDto
    fun getTheatres(ids: List<Long>): List<TheatreViewDto>
}
