package ru.itmo.hls.showmanager.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.hls.showmanager.dto.SeatRawDto

@FeignClient(name = "theatre-manager", contextId = "seatClient", path = "/api/halls")
interface SeatClient {
    @GetMapping("/{id}/seats")
    fun getSeats(@PathVariable("id") hallId: Long): List<SeatRawDto>
}
