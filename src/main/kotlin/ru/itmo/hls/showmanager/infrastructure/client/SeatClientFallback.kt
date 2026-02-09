package ru.itmo.hls.showmanager.infrastructure.client

import org.springframework.stereotype.Component
import ru.itmo.hls.showmanager.application.dto.SeatRawDto

@Component
class SeatClientFallback : SeatClient {
    override fun getSeats(hallId: Long): List<SeatRawDto> = emptyList()
}
