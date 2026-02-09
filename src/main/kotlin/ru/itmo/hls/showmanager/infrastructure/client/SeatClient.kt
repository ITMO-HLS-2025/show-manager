package ru.itmo.hls.showmanager.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.hls.showmanager.application.dto.SeatRawDto

@FeignClient(
    name = "theatre-manager",
    contextId = "seatClient",
    path = "/api/halls",
    fallback = SeatClientFallback::class
)
interface SeatClient : ru.itmo.hls.showmanager.application.port.SeatClient {
    @GetMapping("/{id}/seats")
    override fun getSeats(@PathVariable("id") hallId: Long): List<SeatRawDto>
}
