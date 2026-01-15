package ru.itmo.hls.showmanager.client

import org.springframework.stereotype.Component
import ru.itmo.hls.showmanager.dto.TheatreViewDto

@Component
class TheatreClientFallback : TheatreClient {
    override fun getTheatre(id: Long): TheatreViewDto {
        return TheatreViewDto(
            id = id,
            name = "Unknown",
            city = "Unknown",
            address = "Unknown"
        )
    }

    override fun getTheatres(ids: List<Long>): List<TheatreViewDto> = emptyList()
}
