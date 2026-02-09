package ru.itmo.hls.showmanager.infrastructure.client

import org.springframework.stereotype.Component

@Component
class OrderClientFallback : OrderClient {
    override fun getOccupiedSeats(showId: Long): List<Long> = emptyList()
}
