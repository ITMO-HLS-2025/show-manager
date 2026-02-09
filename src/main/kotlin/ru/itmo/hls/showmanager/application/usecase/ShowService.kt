package ru.itmo.hls.showmanager.application.usecase

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.itmo.hls.showmanager.application.dto.PerformanceCreateDto
import ru.itmo.hls.showmanager.application.dto.PerformanceDto
import ru.itmo.hls.showmanager.application.dto.ShowCreateDto
import ru.itmo.hls.showmanager.application.dto.ShowDto
import ru.itmo.hls.showmanager.application.dto.ShowViewDto
import ru.itmo.hls.showmanager.application.dto.SeatRawDto
import ru.itmo.hls.showmanager.application.port.HallClient
import ru.itmo.hls.showmanager.application.port.OrderClient
import ru.itmo.hls.showmanager.application.port.SeatClient
import ru.itmo.hls.showmanager.application.port.TheatreClient
import ru.itmo.hls.showmanager.domain.model.Performance
import ru.itmo.hls.showmanager.domain.model.Show
import ru.itmo.hls.showmanager.domain.exception.ShowNotFoundException
import ru.itmo.hls.showmanager.application.mapper.toDto
import ru.itmo.hls.showmanager.application.mapper.toViewDto
import ru.itmo.hls.showmanager.domain.port.ShowRepository
import java.time.LocalDateTime

@Service
class ShowService(
    private val showRepository: ShowRepository,
    private val performanceService: PerformanceService,
    private val hallClient: HallClient,
    private val theatreClient: TheatreClient,
    private val seatClient: SeatClient,
    private val orderClient: OrderClient
) {
    private val log = LoggerFactory.getLogger(ShowService::class.java)

    fun getShowInfo(id: Long): ShowDto {
        log.info("Получение информации о шоу id={}", id)

        val show = showRepository.findShowByIdFetch(id)
            ?: throw ShowNotFoundException("Шоу по id $id не найдено")

        val hall = hallClient.getHall(show.hallId)
        if (hall.theatreId <= 0) {
            throw IllegalStateException("Hall ${hall.id} has no theatre")
        }
        val theatre = theatreClient.getTheatre(hall.theatreId)

        return show.toDto(hall, theatre)
    }

    fun getAllShow(city: String, page: Int, pageSize: Int): Page<ShowViewDto> {
        log.info("Получение всех шоу в городе={}, страница={}, размер={}", city, page, pageSize)

        val now = LocalDateTime.now()
        val end = now.plusDays(14)
        val shows = showRepository.findAllByShowTimeBetween(now, end)

        val hallIds = shows.map { it.hallId }.distinct()
        val hallsById = hallIds.associateWith { hallClient.getHall(it) }
        val theatreIds = hallsById.values.mapNotNull { hall ->
            hall.theatreId.takeIf { it > 0 }
        }.distinct()
        val theatresById: Map<Long, ru.itmo.hls.showmanager.application.dto.TheatreViewDto> =
            if (theatreIds.isEmpty()) {
                emptyMap()
            } else {
                theatreClient.getTheatres(theatreIds).associateBy { it.id }
            }

        val filtered = shows.filter { show ->
            val hall = hallsById[show.hallId] ?: return@filter false
            if (hall.theatreId <= 0) return@filter false
            val theatre = theatresById[hall.theatreId] ?: return@filter false
            theatre.city.equals(city, ignoreCase = true)
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
                durationMinutes = dto.durationMinutes
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

        return performanceService.save(performance).toDto()
    }

    fun createShow(dto: ShowCreateDto): ShowDto {
        if (dto.hallId <= 0) {
            throw IllegalArgumentException("Hall id is required")
        }
        if (dto.performanceId <= 0) {
            throw IllegalArgumentException("Performance id is required")
        }
        val performance = performanceService.findById(dto.performanceId)
            ?: throw IllegalArgumentException("Performance not found: id=${dto.performanceId}")

        val show = showRepository.save(
            Show(
                performance = performance,
                hallId = dto.hallId,
                showTime = dto.date
            )
        )

        val hall = hallClient.getHall(dto.hallId)
        if (hall.theatreId <= 0) {
            throw IllegalArgumentException("Hall ${hall.id} has no theatre")
        }
        val theatre = theatreClient.getTheatre(hall.theatreId)
        return show.toDto(hall, theatre)
    }

    fun getAllSeats(showId: Long): List<SeatRawDto> {
        val show = showRepository.findShowByIdFetch(showId)
            ?: throw ShowNotFoundException("Шоу по id $showId не найдено")
        val occupiedSeatIds = orderClient.getOccupiedSeats(showId).toSet()
        val seats = seatClient.getSeats(show.hallId)
        return seats.map { row ->
            row.copy(
                seats = row.seats.map { seat ->
                    if (occupiedSeatIds.contains(seat.seatId)) {
                        seat.copy(status = "OCCUPIED")
                    } else {
                        seat
                    }
                }
            )
        }
    }

    fun findAllByTheatreId(id: Long, now: LocalDateTime, endDate: LocalDateTime): List<Show> {
        val shows = showRepository.findAllByShowTimeBetween(now, endDate)
        val hallIds = shows.map { it.hallId }.distinct()
        val hallsById = hallIds.associateWith { hallClient.getHall(it) }
        return shows.filter { show ->
            val hall = hallsById[show.hallId] ?: return@filter false
            hall.theatreId == id
        }
    }
}
