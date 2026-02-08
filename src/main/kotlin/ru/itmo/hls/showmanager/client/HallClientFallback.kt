package ru.itmo.hls.showmanager.client

import org.springframework.stereotype.Component
import ru.itmo.hls.showmanager.dto.HallViewDto

@Component
class HallClientFallback : HallClient {
    override fun getHall(id: Long): HallViewDto = HallViewDto(id = id, number = -1, theatreId = -1)
}
