package ru.itmo.hls.showmanager.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.jdbc.Sql
import ru.itmo.hls.showmanager.client.HallClient
import ru.itmo.hls.showmanager.client.OrderClient
import ru.itmo.hls.showmanager.client.SeatClient
import ru.itmo.hls.showmanager.client.TheatreClient
import ru.itmo.hls.showmanager.dto.HallViewDto
import ru.itmo.hls.showmanager.dto.PerformanceDto
import ru.itmo.hls.showmanager.dto.ShowCreateDto
import ru.itmo.hls.showmanager.dto.TheatreViewDto
import ru.itmo.hls.showmanager.repository.ShowRepository
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = ["/sql/cleanup.sql", "/sql/show_data.sql"])
class ShowServiceTest {
    @Autowired
    private lateinit var showService: ShowService

    @Autowired
    private lateinit var showRepository: ShowRepository

    @MockitoBean
    private lateinit var hallClient: HallClient

    @MockitoBean
    private lateinit var theatreClient: TheatreClient

    @MockitoBean
    private lateinit var seatClient: SeatClient

    @MockitoBean
    private lateinit var orderClient: OrderClient

    @Test
    fun `getShowInfo returns aggregated data`() {
        `when`(hallClient.getHall(100)).thenReturn(HallViewDto(100, 1))
        `when`(theatreClient.getTheatre(200)).thenReturn(
            TheatreViewDto(200, "Main Theatre", "Spb", "Nevsky 1")
        )

        val dto = showService.getShowInfo(10)

        assertEquals(10, dto.id)
        assertEquals("Hamlet", dto.title)
        assertEquals("Classic drama", dto.description)
        assertEquals(120, dto.durationMinutes)
        assertEquals(100, dto.hall.id)
        assertEquals("Main Theatre", dto.theatre.name)
    }

    @Test
    fun `getAllShow filters by city`() {
        `when`(theatreClient.getTheatres(anyList())).thenReturn(
            listOf(
                TheatreViewDto(200, "Main Theatre", "Spb", "Nevsky 1"),
                TheatreViewDto(201, "Big Stage", "Moscow", "Tverskaya 2")
            )
        )

        val page = showService.getAllShow("Spb", 1, 10)

        assertEquals(1, page.totalElements)
    }

    @Test
    fun `getAllShow returns empty when theatres missing`() {
        `when`(theatreClient.getTheatres(anyList())).thenReturn(emptyList())

        val page = showService.getAllShow("Spb", 1, 10)

        assertEquals(0, page.totalElements)
        assertEquals(0, page.content.size)
    }

    @Test
    fun `getAllShow returns empty page when out of range`() {
        `when`(theatreClient.getTheatres(anyList())).thenReturn(
            listOf(TheatreViewDto(200, "Main Theatre", "Spb", "Nevsky 1"))
        )

        val page = showService.getAllShow("Spb", 2, 10)

        assertEquals(1, page.totalElements)
        assertEquals(0, page.content.size)
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql", "/sql/show_data.sql", "/sql/show_data_multi_theatre.sql"])
    fun `getAllShow matches when any theatre fits`() {
        `when`(theatreClient.getTheatres(anyList())).thenReturn(
            listOf(
                TheatreViewDto(200, "Main Theatre", "Moscow", "Tverskaya 1"),
                TheatreViewDto(201, "Second Stage", "Spb", "Nevsky 2")
            )
        )

        val page = showService.getAllShow("Spb", 1, 10)

        assertEquals(2, page.totalElements)
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql"])
    fun `createShow persists performance and show`() {
        `when`(hallClient.getHall(300)).thenReturn(HallViewDto(300, 5))
        `when`(theatreClient.getTheatre(400)).thenReturn(
            TheatreViewDto(400, "City Theatre", "Kazan", "Bauman 3")
        )

        val dto = ShowCreateDto(
            title = "New Show",
            description = "Fresh play",
            date = LocalDateTime.now(),
            durationMinutes = 90,
            hall = HallViewDto(300, 5),
            theatre = TheatreViewDto(400, "City Theatre", "Kazan", "Bauman 3")
        )

        val created = showService.createShow(dto)

        assertNotNull(created.id)
        assertEquals(1, showRepository.findAll().size)
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql", "/sql/show_data_no_theatre.sql"])
    fun `getShowInfo throws when no theatre ids`() {
        `when`(hallClient.getHall(102)).thenReturn(HallViewDto(102, 1))

        assertThrows(IllegalStateException::class.java) {
            showService.getShowInfo(12)
        }
    }

    @Test
    fun `updatePerformance updates theatre ids`() {
        val dto = PerformanceDto(
            id = 1,
            title = "Hamlet Updated",
            description = "Updated",
            durationMinutes = 110,
            theatreIds = listOf(200, 201)
        )

        val updated = showService.updatePerformance(1, dto)

        assertEquals("Hamlet Updated", updated.title)
        assertEquals(2, updated.theatreIds.size)
    }

    @Test
    fun `updatePerformance throws when missing`() {
        val dto = PerformanceDto(
            id = 999,
            title = "Missing",
            description = null,
            durationMinutes = null,
            theatreIds = emptyList()
        )

        assertThrows(RuntimeException::class.java) {
            showService.updatePerformance(999, dto)
        }
    }
}
