package ru.itmo.hls.showmanager.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.hls.showmanager.client.HallClient
import ru.itmo.hls.showmanager.client.OrderClient
import ru.itmo.hls.showmanager.client.SeatClient
import ru.itmo.hls.showmanager.client.TheatreClient
import ru.itmo.hls.showmanager.dto.HallViewDto
import ru.itmo.hls.showmanager.service.ShowService
import ru.itmo.hls.showmanager.dto.ShowCreateDto
import ru.itmo.hls.showmanager.dto.TheatreViewDto
import ru.itmo.hls.showmanager.dto.PerformanceCreateDto
import ru.itmo.hls.showmanager.dto.PerformanceDto
import ru.itmo.hls.showmanager.dto.SeatRawDto
import ru.itmo.hls.showmanager.dto.SeatStatusDto
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = ["/sql/cleanup.sql", "/sql/show_data.sql"])
class ShowControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var showService: ShowService

    @MockitoBean
    private lateinit var hallClient: HallClient

    @MockitoBean
    private lateinit var theatreClient: TheatreClient

    @MockitoBean
    private lateinit var seatClient: SeatClient

    @MockitoBean
    private lateinit var orderClient: OrderClient

    @Test
    fun `get show returns payload`() {
        `when`(hallClient.getHall(100)).thenReturn(HallViewDto(100, 1, 200))
        `when`(theatreClient.getTheatre(200)).thenReturn(
            TheatreViewDto(200, "Main Theatre", "Spb", "Nevsky 1")
        )

        mockMvc.perform(get("/api/shows/10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.title").value("Hamlet"))
            .andExpect(jsonPath("$.description").value("Classic drama"))
            .andExpect(jsonPath("$.hall.id").value(100))
            .andExpect(jsonPath("$.theatre.city").value("Spb"))
    }

    @Test
    fun `get shows filters by city`() {
        `when`(hallClient.getHall(100)).thenReturn(HallViewDto(100, 1, 200))
        `when`(hallClient.getHall(101)).thenReturn(HallViewDto(101, 2, 201))
        `when`(theatreClient.getTheatres(anyList())).thenReturn(
            listOf(
                TheatreViewDto(200, "Main Theatre", "Spb", "Nevsky 1"),
                TheatreViewDto(201, "Big Stage", "Moscow", "Tverskaya 2")
            )
        )

        mockMvc.perform(get("/api/shows").param("city", "Spb").param("page", "1").param("pageSize", "10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].title").value("Hamlet"))
            .andExpect(header().string("X-Total-Count", "1"))
            .andExpect(header().string("X-Page-Number", "1"))
            .andExpect(header().string("X-Page-Size", "10"))
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql"])
    fun `create show persists and returns`() {
        `when`(hallClient.getHall(300)).thenReturn(HallViewDto(300, 5, 400))
        `when`(theatreClient.getTheatre(400)).thenReturn(
            TheatreViewDto(400, "City Theatre", "Kazan", "Bauman 3")
        )

        val performance = showService.createPerformance(
            PerformanceCreateDto(
                title = "New Performance",
                description = "Fresh play",
                durationMinutes = 90
            )
        )

        val dto = ShowCreateDto(
            date = LocalDateTime.now(),
            performanceId = performance.id,
            hallId = 300
        )

        mockMvc.perform(
            post("/api/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("New Performance"))
            .andExpect(jsonPath("$.hall.id").value(300))
            .andExpect(jsonPath("$.theatre.id").value(400))
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql"])
    fun `get show returns 404 when missing`() {
        mockMvc.perform(get("/api/shows/999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Шоу по id 999 не найдено"))
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql"])
    fun `create show returns 400 when hall id missing`() {
        val dto = ShowCreateDto(
            date = LocalDateTime.now(),
            performanceId = 400,
            hallId = 0
        )

        mockMvc.perform(
            post("/api/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Hall id is required"))
    }

    @Test
    fun `get seats returns payload`() {
        `when`(seatClient.getSeats(100)).thenReturn(
            listOf(
                SeatRawDto(1, listOf(SeatStatusDto(1, 1, "FREE")))
            )
        )
        `when`(orderClient.getOccupiedSeats(10)).thenReturn(emptyList())

        mockMvc.perform(get("/api/shows/10/seats").param("page", "1").param("pageSize", "10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].row").value(1))
            .andExpect(jsonPath("$[0].seats[0].seatId").value(1))
    }

    @Test
    fun `get seats returns 400 on invalid page size`() {
        mockMvc.perform(get("/api/shows/10/seats").param("page", "1").param("pageSize", "0"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Invalid page size"))
    }

    @Test
    fun `get shows returns 400 on invalid page size`() {
        mockMvc.perform(get("/api/shows").param("city", "Spb").param("page", "1").param("pageSize", "0"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Invalid page size"))
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql"])
    fun `create performance returns payload`() {
        val dto = PerformanceCreateDto(
            title = "New Performance",
            description = "Desc",
            durationMinutes = 80
        )

        mockMvc.perform(
            post("/api/shows/performance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("New Performance"))
    }

    @Test
    fun `update performance returns updated payload`() {
        val dto = PerformanceDto(
            id = 1,
            title = "Hamlet Updated",
            description = "Updated",
            durationMinutes = 110
        )

        mockMvc.perform(
            put("/api/shows/performance/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Hamlet Updated"))
    }
}
