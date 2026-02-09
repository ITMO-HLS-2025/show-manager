package ru.itmo.hls.showmanager.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.hls.showmanager.application.dto.HallViewDto

@FeignClient(
    name = "theatre-manager",
    contextId = "hallClient",
    path = "/api/halls",
    fallback = HallClientFallback::class
)
interface HallClient : ru.itmo.hls.showmanager.application.port.HallClient {
    @GetMapping("/{id}")
    override fun getHall(@PathVariable("id") id: Long): HallViewDto
}
