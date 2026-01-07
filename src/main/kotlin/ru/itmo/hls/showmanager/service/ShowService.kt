package ru.itmo.hls.showmanager.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.itmo.hls.showmanager.client.HallClient
import ru.itmo.hls.showmanager.client.SeatClient
import ru.itmo.hls.showmanager.client.TheatreClient
import ru.itmo.hls.showmanager.dto.PerformanceCreateDto
import ru.itmo.hls.showmanager.dto.PerformanceDto
import ru.itmo.hls.showmanager.dto.ShowCreateDto
import ru.itmo.hls.showmanager.dto.ShowDto
import ru.itmo.hls.showmanager.dto.ShowViewDto
import ru.itmo.hls.showmanager.dto.SeatRawDto
import ru.itmo.hls.showmanager.entity.Performance
import ru.itmo.hls.showmanager.entity.Show
import ru.itmo.hls.showmanager.exception.ShowNotFoundException
import ru.itmo.hls.showmanager.mapper.toDto
import ru.itmo.hls.showmanager.mapper.toViewDto
import ru.itmo.hls.showmanager.repository.ShowRepository
import java.time.LocalDateTime

@Service
class ShowService(
    private val showRepository: ShowRepository,
    private val performanceService: PerformanceService,
    private val hallClient: HallClient,
    private val theatreClient: TheatreClient,
    private val seatClient: SeatClient
) {
    private val log = LoggerFactory.getLogger(ShowService::class.java)

    fun getShowInfo(id: Long): ShowDto {
        log.info("Получение информации о шоу id={}", id)

        val show = showRepository.findShowByIdFetch(id)
            ?: throw ShowNotFoundException("Шоу по id $id не найдено")

        val hall = hallClient.getHall(show.hallId)
        val theatreId = show.performance.theatreIds.firstOrNull()
            ?: throw IllegalStateException("Performance ${show.performance.id} has no theatres")
        val theatre = theatreClient.getTheatre(theatreId)

        return show.toDto(hall, theatre)
    }

    fun getAllShow(city: String, page: Int, pageSize: Int): Page<ShowViewDto> {
        log.info("Получение всех шоу в городе={}, страница={}, размер={}", city, page, pageSize)

        val now = LocalDateTime.now()
        val end = now.plusDays(14)
        val shows = showRepository.findAllByShowTimeBetween(now, end)

        val theatreIds = shows.flatMap { it.performance.theatreIds }.distinct()
        val theatresById = if (theatreIds.isEmpty()) {
            emptyMap()
        } else {
            theatreClient.getTheatres(theatreIds).associateBy { it.id }
        }

        val filtered = shows.filter { show ->
            show.performance.theatreIds.any { theatreId ->
                val theatre = theatresById[theatreId] ?: return@any false
                theatre.city.equals(city, ignoreCase = true)
            }
        }

        val fromIndex = ((page - 1) * pageSize).coerceAtLeast(0)
        val toIndex = (fromIndex + pageSize).coerceAtMost(filtered.size)
        val pageContent = if (fromIndex >= filtered.size) emptyList() else filtered.subList(fromIndex, toIndex)

        return PageImpl(
            pageContent.map { it.toViewDto() },
            PageRequest.of(page - 1, pageSize),
            filtered.size.toLong()
        )
    }

    fun createPerformance(dto: PerformanceCreateDto): PerformanceDto {
        val entity = performanceService.save(
            Performance(
                title = dto.title,
                description = dto.description,
                durationMinutes = dto.durationMinutes,
                theatreIds = dto.theatreIds.toMutableSet()
            )
        )
        return entity.toDto()
    }

    fun updatePerformance(id: Long, dto: PerformanceDto): PerformanceDto {
        val performance = performanceService.findById(id)
            ?: throw RuntimeException("Performance not found")

        performance.title = dto.title
        performance.description = dto.description
        performance.durationMinutes = dto.durationMinutes
        performance.theatreIds = dto.theatreIds.toMutableSet()

        return performanceService.save(performance).toDto()
    }

    fun createShow(dto: ShowCreateDto): ShowDto {
        val hallId = dto.hall.id
            ?: throw IllegalArgumentException("Hall id is required")
        val theatreId = dto.theatre.id

        val performance = performanceService.save(
            Performance(
                title = dto.title,
                description = dto.description,
                durationMinutes = dto.durationMinutes,
                theatreIds = mutableSetOf(theatreId)
            )
        )

        val show = showRepository.save(
            Show(
                performance = performance,
                hallId = hallId,
                showTime = dto.date
            )
        )

        val hall = hallClient.getHall(hallId)
        val theatre = theatreClient.getTheatre(theatreId)
        return show.toDto(hall, theatre)
    }

    fun getAllSeats(showId: Long): List<SeatRawDto> {
        val show = showRepository.findShowByIdFetch(showId)
            ?: throw ShowNotFoundException("Шоу по id $showId не найдено")
        return seatClient.getSeats(show.hallId)
    }

    fun findAllByTheatreId(id: Long, now: LocalDateTime, endDate: LocalDateTime): List<Show> {
        return showRepository.findAllByTheatreId(id, now, endDate)
    }
}
