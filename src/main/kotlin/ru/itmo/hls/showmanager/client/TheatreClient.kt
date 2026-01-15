package ru.itmo.hls.showmanager.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.itmo.hls.showmanager.dto.TheatreViewDto

@FeignClient(
    name = "theatre-manager",
    contextId = "theatreClient",
    path = "/api/theatres",
    fallback = TheatreClientFallback::class
)
interface TheatreClient {
    @GetMapping("/{id}")
    fun getTheatre(@PathVariable("id") id: Long): TheatreViewDto

    @PostMapping("/batch")
    fun getTheatres(@RequestBody ids: List<Long>): List<TheatreViewDto>
}
