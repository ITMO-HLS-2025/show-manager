package ru.itmo.hls.showmanager.application.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.itmo.hls.showmanager.application.dto.HallViewDto
import ru.itmo.hls.showmanager.application.dto.TheatreViewDto
import ru.itmo.hls.showmanager.domain.model.Performance
import ru.itmo.hls.showmanager.domain.model.Show
import java.time.LocalDateTime

class ShowMapperTest {
    @Test
    fun `toDto maps fields`() {
        val performance = Performance(
            id = 1,
            title = "Hamlet",
            description = "Desc",
            durationMinutes = 120
        )
        val show = Show(
            id = 10,
            performance = performance,
            hallId = 100,
            showTime = LocalDateTime.of(2030, 1, 1, 19, 0)
        )
        val hall = HallViewDto(100, 1, 200)
        val theatre = TheatreViewDto(200, "Main Theatre", "Spb", "Nevsky 1")

        val dto = show.toDto(hall, theatre)

        assertEquals(10, dto.id)
        assertEquals("Hamlet", dto.title)
        assertEquals("Desc", dto.description)
        assertEquals(120, dto.durationMinutes)
        assertEquals(hall, dto.hall)
        assertEquals(theatre, dto.theatre)
    }

    @Test
    fun `toViewDto maps fields`() {
        val performance = Performance(
            id = 1,
            title = "Hamlet",
            description = null,
            durationMinutes = 120
        )
        val show = Show(
            id = 10,
            performance = performance,
            hallId = 100,
            showTime = LocalDateTime.of(2030, 1, 1, 19, 0)
        )

        val dto = show.toViewDto()

        assertEquals(10, dto.id)
        assertEquals("Hamlet", dto.title)
    }
}
