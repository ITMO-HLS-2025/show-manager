package ru.itmo.hls.showmanager.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.format.annotation.DateTimeFormat
import ru.itmo.hls.showmanager.dto.PerformanceCreateDto
import ru.itmo.hls.showmanager.dto.PerformanceDto
import ru.itmo.hls.showmanager.dto.ShowCreateDto
import ru.itmo.hls.showmanager.dto.ShowDto
import ru.itmo.hls.showmanager.dto.ShowViewDto
import ru.itmo.hls.showmanager.dto.SeatRawDto
import ru.itmo.hls.showmanager.mapper.toViewDto
import ru.itmo.hls.showmanager.service.ShowService
import ru.itmo.hls.showmanager.validation.PaginationValidator

@RestController
@RequestMapping("/api/shows")
class ShowController(
    private val showService: ShowService,
    private val paginationValidator: PaginationValidator
) {
    @GetMapping("/{id}")
    fun getShow(@PathVariable id: Long): ShowDto = showService.getShowInfo(id)

    @GetMapping
    fun getAllShows(
        @RequestParam city: String,
        @RequestParam page: Int,
        @RequestParam pageSize: Int
    ): org.springframework.http.ResponseEntity<List<ShowViewDto>> {
        paginationValidator.validateSize(pageSize)
        val resultPage = showService.getAllShow(city, page, pageSize)
        return org.springframework.http.ResponseEntity.ok()
            .header("X-Total-Count", resultPage.totalElements.toString())
            .header("X-Page-Number", (resultPage.number + 1).toString())
            .header("X-Page-Size", resultPage.size.toString())
            .body(resultPage.content)
    }

    @PostMapping
    fun createShow(@RequestBody dto: ShowCreateDto): ShowDto = showService.createShow(dto)

    @GetMapping("/{id}/seats")
    fun getAllSeatsPaged(
        @PathVariable id: Long,
        @RequestParam page: Int = 1,
        @RequestParam pageSize: Int = 10
    ): org.springframework.http.ResponseEntity<List<SeatRawDto>> {
        paginationValidator.validateSize(pageSize)
        val seats = showService.getAllSeats(id)
        return org.springframework.http.ResponseEntity.ok(seats)
    }

    @GetMapping("/theatre/{theatreId}")
    fun getShowsByTheatre(
        @PathVariable theatreId: Long,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) from: java.time.LocalDateTime,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) to: java.time.LocalDateTime
    ): List<ShowViewDto> =
        showService.findAllByTheatreId(theatreId, from, to).map { it.toViewDto() }

    @PostMapping("/performance")
    fun createPerformance(@RequestBody dto: PerformanceCreateDto): PerformanceDto =
        showService.createPerformance(dto)

    @PutMapping("/performance/{id}")
    fun updatePerformance(
        @PathVariable id: Long,
        @RequestBody dto: PerformanceDto
    ): PerformanceDto = showService.updatePerformance(id, dto)
}
