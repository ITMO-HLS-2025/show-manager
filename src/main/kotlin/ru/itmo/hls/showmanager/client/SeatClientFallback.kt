package ru.itmo.hls.showmanager.client

import org.springframework.stereotype.Component
import ru.itmo.hls.showmanager.dto.SeatRawDto

@Component
class SeatClientFallback : SeatClient {
    override fun getSeats(hallId: Long): List<SeatRawDto> = emptyList()
}
