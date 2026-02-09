package ru.itmo.hls.showmanager.application.port

import ru.itmo.hls.showmanager.application.dto.SeatRawDto

interface SeatClient {
    fun getSeats(hallId: Long): List<SeatRawDto>
}
