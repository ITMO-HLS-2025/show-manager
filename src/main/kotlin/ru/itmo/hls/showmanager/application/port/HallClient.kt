package ru.itmo.hls.showmanager.application.port

import ru.itmo.hls.showmanager.application.dto.HallViewDto

interface HallClient {
    fun getHall(id: Long): HallViewDto
}
