package ru.itmo.hls.showmanager.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "order-service",
    contextId = "orderClient",
    path = "/inner/shows",
    fallback = OrderClientFallback::class
)
interface OrderClient : ru.itmo.hls.showmanager.application.port.OrderClient {
    @GetMapping("/{showId}/occupied-seats")
    override fun getOccupiedSeats(@PathVariable("showId") showId: Long): List<Long>
}
