package ru.itmo.hls.showmanager.client

import org.springframework.stereotype.Component

@Component
class OrderClientFallback : OrderClient {
    override fun getOccupiedSeats(showId: Long): List<Long> = emptyList()
}
