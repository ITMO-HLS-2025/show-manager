package ru.itmo.hls.showmanager.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.hls.showmanager.dto.HallViewDto

@FeignClient(
    name = "theatre-manager",
    contextId = "hallClient",
    path = "/api/halls",
    fallback = HallClientFallback::class
)
interface HallClient {
    @GetMapping("/{id}")
    fun getHall(@PathVariable("id") id: Long): HallViewDto
}
